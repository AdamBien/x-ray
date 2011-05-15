package com.abien.xray.probe.http;

import com.abien.xray.probe.monitoring.JMXRegistry;
import com.abien.xray.probe.monitoring.XRayMonitoring;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author blog.adam-bien.com
 */
public class HTTPRequestRESTInterceptor implements Filter {

    private final static Logger LOG = Logger.getLogger(HTTPRequestRESTInterceptor.class.getName());
    private String serviceURL;
    public static final String SERVICE_URL_KEY = "serviceURL";
    URL url;
    RESTClient client;
    static final String REFERER = "referer";
    static final String DELIMITER = "|";
    static Executor executor = null;
    private volatile static AtomicInteger nrOfRejectedJobs = new AtomicInteger(0);
    private volatile static long xrayPerformance = -1;
    private volatile static long worstXrayPerformance = -1;
    private volatile static long applicationPerformance = -1;
    private volatile static long worstApplicationPerformance = -1;
    public final static int NR_OF_THREADS = 2;
    public final static int QUEUE_CAPACITY = 5;

    static {
        setupThreadPools();
        registerMonitoring();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.serviceURL = filterConfig.getInitParameter(SERVICE_URL_KEY);
        if(this.serviceURL == null){
            LOG.severe("ServiceURL not set - check the web.xml and set the value for key:" + SERVICE_URL_KEY);
            return;
        }
        try {
            this.url = new URL(this.serviceURL);
            this.client = new RESTClient(this.url);
        } catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, "Problem with serviceUrl {0}", ex);
        }
    }

    private static void setupThreadPools() {
        MonitorableThreadFactory monitorableThreadFactory = new MonitorableThreadFactory();
        RejectedExecutionHandler ignoringHandler = new RejectedExecutionHandler()  {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                int rejectedJobs = nrOfRejectedJobs.incrementAndGet();
                LOG.log(Level.SEVERE, "Job: {0} rejected. Number of rejected jobs: {1}", new Object[]{r, rejectedJobs});

            }
        };
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(QUEUE_CAPACITY);
        executor = new ThreadPoolExecutor(NR_OF_THREADS, NR_OF_THREADS, Integer.MAX_VALUE, TimeUnit.SECONDS, workQueue, monitorableThreadFactory, ignoringHandler);
    }

    private static void registerMonitoring() {
        new JMXRegistry().rebind(XRayMonitoring.JMX_NAME, new XRayMonitoring());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String uri = httpServletRequest.getRequestURI();
        Map<String, String> headers = extractHeaders(httpServletRequest);
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        applicationPerformance = (System.currentTimeMillis() - start);
        worstApplicationPerformance = Math.max(applicationPerformance, worstApplicationPerformance);
        sendAsync(uri, headers);
    }
    
    Map<String,String> extractHeaders(HttpServletRequest httpServletRequest){
        Map<String,String> headers = new HashMap<String, String>();
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        if(headerNames == null){
            return headers;
        }
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String header = httpServletRequest.getHeader(name);
            headers.put(name,header);
        }
        return headers;
    }

    public void sendAsync(final String uri, final Map<String, String> headers) {
        Runnable runnable = getInstrumentedRunnable(uri, headers);
        String actionName = createName(uri, headers.get(REFERER));
        executor.execute(new ThreadNameTrackingRunnable(runnable, actionName));
    }
    
    public Runnable getInstrumentedRunnable(final String uri, final Map<String,String> headers) {
        return new Runnable()  {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                send(uri, headers);
                xrayPerformance = (System.currentTimeMillis() - start);
                worstXrayPerformance = Math.max(xrayPerformance, worstXrayPerformance);
            }
        };
    }    

    String createName(final String uri, final String referer) {
        return uri + "|" + referer;
    }

    public void send(final String uri, final Map<String, String> headers) {
        String message = createMessage(uri, headers.get(REFERER));
        client.put(message,headers);
    }

    String createMessage(String uri, String referer) {
        if (referer == null) {
            return uri;
        }
        return uri + DELIMITER + referer;
    }

    public static int getNrOfRejectedJobs() {
        return nrOfRejectedJobs.get();
    }

    public static long getApplicationPerformance() {
        return applicationPerformance;
    }

    public static long getXRayPerformance() {
        return xrayPerformance;
    }

    public static long getWorstApplicationPerformance() {
        return worstApplicationPerformance;
    }

    public static long getWorstXRayPerformance() {
        return worstXrayPerformance;
    }

    public static void resetStatistics() {
        worstApplicationPerformance = 0;
        worstXrayPerformance = 0;
        applicationPerformance = 0;
        xrayPerformance = 0;
        nrOfRejectedJobs.set(0);
    }

    @Override
    public void destroy() {
    }

}

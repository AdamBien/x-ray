package com.abien.xray;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class XRay {

    private String baseUrl;
    public final static String HITS_PER_HOUR = "hitsperhour";
    public final static String MOST_POPULAR = "mostpopular";
    public final static String HITS_PER_MINUTE = "hitsperminute";
    public final static String TOTAL_HITS = "hits";
    public final static String HITS_FOR_URI = "hits/";
    public final static String TRENDING = "trending?max=5";
    public final static String TODAY_HITS = "hitsperday/today";

    public final static String YESTERDAY_HITS = "hitsperday/yesterday";
    private final static Logger LOG = Logger.getLogger(XRay.class.getName());
    private static final int TIMEOUT_IN_MS = 100;

    public XRay(String url) {
        this.baseUrl = url;
    }

    public String getHitsPerHour() {
        String uri = getUrl(HITS_PER_HOUR);
        return getContent(uri);
    }
    
    public String getHitsPerMinute(){
        String uri = getUrl(HITS_PER_MINUTE);
        return getContent(uri);
    }
    
    public String getTotalHits(){
        String uri = getUrl(TOTAL_HITS);
        return getContent(uri);
    }

    public String getHitsForPost(String post) {
        String uri = getUrl(HITS_FOR_URI);
        String encoded;
        try {
            encoded = URLEncoder.encode(post, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.log(Level.SEVERE, "Cannot encode URI: {0} Reason: {1}", new Object[]{post, e});
            return "--";
        }
        String postURL = uri + encoded;
        return getContent(postURL);
    }


    public String getMostPopularAsHtml(){
        String uri = getUrl(MOST_POPULAR);
        return getContent(uri);
    }

    public String getTrendingAsHtml(){
        String uri = getUrl(TRENDING);
        return getContent(uri);
    }
    
    public String getTodayHits(){
        String uri = getUrl(TODAY_HITS);
        return getContent(uri);
    }

    public String getYesterdayHits(){
        String uri = getUrl(YESTERDAY_HITS);
        return getContent(uri);
    }

    String getContent(String uri){
            try {
            URL url = new URL(uri);
            return getContent(url);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Cannot connect to X-Ray-Services: {0} Reason: {1}", new Object[]{uri, ex});
            return "--";
        }
    }

    String getContent(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(TIMEOUT_IN_MS);
        urlConnection.setReadTimeout(TIMEOUT_IN_MS);
        InputStream stream = urlConnection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuilder content = new StringBuilder("");
        while ((line = br.readLine()) != null) {
            content.append(line);
        }
        br.close();
        return content.toString();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    String getUrl(String command) {
        return baseUrl + command;

    }
}

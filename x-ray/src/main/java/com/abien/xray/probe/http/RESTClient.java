package com.abien.xray.probe.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class RESTClient {

    public static final String PATH = "/x-ray/resources/hits";
    private InetAddress inetAddress;
    private int port;
    private String path;
    private static final Logger logger = Logger.getLogger(RESTClient.class.getName());
    public static final String HEADER_NAME_PREFIX = "xray_";
    private static final int TIMEOUT = 1000;

    public RESTClient(String hostName, int port, String path) {
        try {
            this.inetAddress = InetAddress.getByName(hostName);
            this.port = port;
            this.path = path;
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException("Wrong: " + hostName + " Reason: " + ex, ex);
        }
    }

    public RESTClient(URL url) {
        this(url.getHost(), url.getPort(), url.getPath());
    }

    public RESTClient() {
        this("localhost", 8080, PATH);
    }

    public String put(String content, Map<String,String> headers) {
            Socket socket = null;
            BufferedWriter wr = null;
            InputStreamReader is = null;
        try {
            socket = new Socket(inetAddress, port);
            socket.setSoTimeout(TIMEOUT);
            wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
            is = new InputStreamReader(socket.getInputStream());
            wr.write("PUT " + path + " HTTP/1.0\r\n");
            wr.write(getFormattedHeader("Content-Length","" + content.length()));
            wr.write(getFormattedHeader("Content-Type", "text/plain"));
            for (Map.Entry<String, String> header : headers.entrySet()) {
                wr.write(getFormattedHeader(HEADER_NAME_PREFIX + header.getKey(),header.getValue()));
            }
            wr.write("\r\n");
            wr.write(content);
            wr.flush();
            char[] buffer = new char[1024];
            StringWriter stringWriter = new StringWriter();
            while(is.read(buffer)!=-1){
                stringWriter.write(buffer);
            }
            return stringWriter.toString();
        } catch (Exception e) {
           logger.log(Level.SEVERE, "Problem communicating with x-ray services: {0}", e);
           return "--";
        }finally{
            try {
                wr.close();
            } catch (IOException ex) {
            }
            try {
                is.close();
            } catch (IOException ex) {
            }
            try {
                socket.close();
            } catch (IOException ex) {
            }

        }

    }

    String getFormattedHeader(String key,String value){
        return key + ": " + value + "\r\n";
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }
}

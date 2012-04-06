package com.abien.xray.projector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class BeamFetcher {

    private final URL url;
    private int TIMEOUT_IN_MS = 10000;

    public BeamFetcher(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid URL: " + url,ex);
        }
    }

    public String getSnapshot() {
        try {
            return getContent(this.url);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot fetch content from: " + this.url);
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

    public static void main(String[] args) throws Exception {
        final String uri = "http://localhost:8080/lightfish/live";
        for (int i = 0; i < 10; i++) {
            BeamFetcher fetcher = new BeamFetcher(uri);
            System.out.println("--- " + fetcher.getSnapshot());
        }
    }
}

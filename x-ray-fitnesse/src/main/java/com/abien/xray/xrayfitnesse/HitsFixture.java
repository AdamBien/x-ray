package com.abien.xray.xrayfitnesse;

import com.abien.xray.XRay;
import com.abien.xray.probe.http.RESTClient;
import fitlibrary.DoFixture;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class HitsFixture extends DoFixture{
    public static final String INTEGRATION_SERVER_URI = "http://localhost:5380/x-ray/resources/";

    private RESTClient client;
    private XRay ray;
    
    private long initialTotalHits;
    private long initialTodayHits;
    
    
    public HitsFixture() throws MalformedURLException {
        URL url = new URL(INTEGRATION_SERVER_URI+"hits");
        this.client = new RESTClient(url);
        this.ray = new XRay(INTEGRATION_SERVER_URI);
    }

    public void initializeCounter(){
        this.initialTotalHits = Long.parseLong(this.ray.getTotalHits());
        this.initialTodayHits = Long.parseLong(this.ray.getTodayHits());
    }
        
        
    public boolean totalHitsAre(String hits){
        final String totalHits = computeTotalHits(this.ray.getTotalHits());
        return hits.equals(totalHits);
    }
    
    public boolean todayHitsAre(String hits){
        final String todayHits = computeTodayHits(this.ray.getTodayHits());
        return hits.equals(todayHits);
    }
    
    public void sendURL(String url){
        this.client.put(url, mockHeaders());
    }

    Map<String,String> mockHeaders(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("referer", "hugo");
        map.put("user-agent","netscape");
        return map;
    }

    private String computeTotalHits(String hits) {
        return String.valueOf((Long.parseLong(hits) - this.initialTotalHits));
    }

    private String computeTodayHits(String hits) {
        return String.valueOf((Long.parseLong(hits) - this.initialTodayHits));
    }
}

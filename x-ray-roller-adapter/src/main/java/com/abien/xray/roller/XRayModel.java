package com.abien.xray.roller;

import com.abien.xray.XRay;
import java.util.Map;
import org.apache.roller.weblogger.ui.rendering.model.Model;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class XRayModel implements Model{
    public static final String XRAYMODEL_NAME = "xraymodel";

    private Map configuration;
    private XRay xray;
    public final static String URL = "http://192.168.0.50:5380/x-ray/resources/";
    public final static String XRAYURL = "XRAY-URL";



    @Override
    public void init(Map map) {
        this.configuration = map;
        this.xray = initializeXRay(this.configuration);
    }

    @Override
    public String getModelName() {
        return XRAYMODEL_NAME;
    }

    public XRay getXray(){
        return xray;
    }

    String extractUrl(Map map){
        if(map == null){
            return URL;
        }
        String url = (String) map.get(XRAYURL);
        if(url != null){
            return url;
        }
        return URL;
    }

    XRay initializeXRay(Map map) {
        String url = extractUrl(map);
        return new XRay(url);
    }
}

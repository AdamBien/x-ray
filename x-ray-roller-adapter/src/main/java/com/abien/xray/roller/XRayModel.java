package com.abien.xray.roller;

import com.abien.xray.XRay;
import java.util.HashMap;
import java.util.Map;
import org.apache.roller.weblogger.ui.rendering.model.Model;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class XRayModel implements Model {

    public static final String XRAYMODEL_NAME = "xraymodel";

    private Map configuration;
    private XRay xray;
    public final static String URL = "http://localhost:5380/x-ray/resources/";
    public final static String XRAYURL = "XRAY-URL";

    @Override
    public void init(Map map) {
        this.configuration = map;
        if (this.configuration == null) {
            this.configuration = new HashMap();
        }
        String customUri = System.getProperty(XRAYURL);
        if (customUri != null) {
            this.configuration.put(XRAYURL, customUri);
        }
        this.xray = initializeXRay(this.configuration);
    }

    @Override
    public String getModelName() {
        return XRAYMODEL_NAME;
    }

    public XRay getXray() {
        return xray;
    }

    String extractUrl(Map map) {
        if (map == null) {
            return URL;
        }
        String url = (String) map.get(XRAYURL);
        if (url != null) {
            return url;
        }
        return URL;
    }

    XRay initializeXRay(Map map) {
        String url = extractUrl(map);
        System.out.println("---Effective uri: " + url);
        return new XRay(url);
    }
}

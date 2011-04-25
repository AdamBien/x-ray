package com.abien.xray.business.store.control;

import javax.ws.rs.core.HttpHeaders;
import java.util.Map;

/**
 * User: blog@adam-bien.com
 * Date: 06.01.11
 * Time: 19:43
 */
public class HttpHeaderFilter {
    public final static String HEADER_PREFIX = "xray_";
    public final static String[] NOT_ALLOWED_AGENTs = {"robot"};

    private static final String USER_AGENT_HEADER_NAME = HEADER_PREFIX + HttpHeaders.USER_AGENT.toLowerCase();

    public boolean ignore(Map<String,String> headers){
        if(headers == null || headers.isEmpty()){
            return true;
        }
        if(isRobot(headers)){
            return true;
        }
        return false;
    }

    boolean isRobot(Map<String, String> headers) {
        String userAgent = headers.get(USER_AGENT_HEADER_NAME);
        if(userAgent == null){
            return true;
        }
        userAgent = userAgent.toLowerCase();
        for (String agent : NOT_ALLOWED_AGENTs) {
            if(userAgent.contains(agent)){
                return true;
                }
        }
        return false;
    }
}

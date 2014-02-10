/*
 */
package com.abien.xray.business;

/**
 *
 * @author adam-bien.com
 */
public class ServerLocation {

    static final String XRAY_SERVER = "XRAY_SERVER";
    static final String DEFAULT_HOST = "http://localhost:8080";

    /**
     *
     * @return either the environment entry, the system property or a default
     * value.
     */
    public static String getLocation() {
        String hostName = System.getenv(XRAY_SERVER);
        if (hostName != null) {
            System.out.println("Returning environment entry: " + hostName);
            return hostName;
        } else {
            hostName = System.getProperty(XRAY_SERVER, DEFAULT_HOST);
            System.out.println("Returning default: " + hostName);
        }
        return hostName;
    }

}

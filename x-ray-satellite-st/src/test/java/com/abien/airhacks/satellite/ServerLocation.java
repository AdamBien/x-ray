/*
 */
package com.abien.airhacks.satellite;

/**
 *
 * @author adam-bien.com
 */
public class ServerLocation {

    static final String SATELLITE_SERVER = "SATELLITE_SERVER";
    static final String DEFAULT_HOST = "http://localhost:8080";

    /**
     *
     * @return either the environment entry, the system property or a default
     * value.
     */
    public static String getLocation() {
        String hostName = System.getenv(SATELLITE_SERVER);
        if (hostName != null) {
            System.out.println("Returning environment entry: " + hostName);
            return hostName;
        } else {
            hostName = System.getProperty(SATELLITE_SERVER, DEFAULT_HOST);
            System.out.println("Returning default: " + hostName);
        }
        return hostName;
    }

}

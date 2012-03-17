package com.abien.xray.business.push.entity;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

/**
 *
 * @author adam bien, adam-bien.com
 */
public class Beam {

    private AsyncContext context;

    public Beam(AsyncContext context) {
        this.context = context;
    }

    public void on(String uri) {
        try {
            PrintWriter writer = getWriter();
            writer.println(uri);
            writer.flush();
            this.context.complete();
        } catch (Exception e) {
            System.err.println("Cannot complete context: " + e);
        }
    }

    public PrintWriter getWriter() {
        try {
            final ServletResponse response = this.context.getResponse();
            response.setContentType("plain/text");
            return response.getWriter();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot return writer: " + ex, ex);
        }
    }
}

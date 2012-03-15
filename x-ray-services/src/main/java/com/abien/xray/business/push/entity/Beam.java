package com.abien.xray.business.push.entity;

import java.io.IOException;
import java.io.Writer;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Stateless
public class Beam {

    private AsyncContext context;

    public Beam(AsyncContext context) {
        this.context = context;
    }

    @Asynchronous
    public void on(String uri) {
        this.send(uri);
    }
    
    
    public void send(String uri){
        try{
            this.context.complete();
        }catch(Exception e){
            System.err.println("Cannot complete context: " + e);
        }
    }
    
    public Writer getWriter(){
        try {
            final ServletResponse response = this.context.getResponse();
            response.setContentType("plain/text");
            return response.getWriter();
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot return writer: " + ex,ex);
        }
    }

    
}

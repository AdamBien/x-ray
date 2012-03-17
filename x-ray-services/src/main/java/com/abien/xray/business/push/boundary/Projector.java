package com.abien.xray.business.push.boundary;

import com.abien.xray.business.push.entity.Beam;
import java.io.IOException;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author adam bien, adam-bien.com
 */
@WebServlet(name = "Beamer", urlPatterns = {"/beam"},asyncSupported=true)
public class Projector extends HttpServlet {
    
    @Inject
    Event<Beam> beam;
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AsyncContext rtRequest = request.startAsync();
        beam.fire(new Beam(rtRequest));
        
    }
    
}

package com.abien.xray.business.push.control;

import com.abien.xray.business.push.entity.Beam;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Stateless
public class Bulp {
    
    @Asynchronous
    public void flash(Beam beam, String message){
        beam.on(message);
    }
}

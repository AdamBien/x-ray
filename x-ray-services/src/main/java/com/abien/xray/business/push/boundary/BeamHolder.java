package com.abien.xray.business.push.boundary;

import com.abien.xray.business.push.control.Bulp;
import com.abien.xray.business.push.entity.Beam;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author adam bien, adam-bien.com
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class BeamHolder {
    
    private CopyOnWriteArrayList<Beam> beams;
    
    @Inject
    Bulp bulp;
    
    @PostConstruct
    public void onInit(){
        this.beams = new CopyOnWriteArrayList<Beam>();
    }
    
    public void onNewRequest(@Observes String uri){
        for (Beam beam : beams) {
            bulp.flash(beam,uri);
            beams.remove(beam);
        }
    }
    
    public void onNewBeam(@Observes Beam beam){
        this.beams.add(beam);
    }
    
    @Schedule(minute="*",hour="*",second="*/2")
    public void clearDanglingBeams(){
        beams.clear();
    }
}

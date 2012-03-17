package com.abien.xray.business.push.boundary;

import com.abien.xray.business.push.control.Bulp;
import com.abien.xray.business.push.entity.Beam;
import java.util.Iterator;
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
public class Lamp {

    private CopyOnWriteArrayList<Beam> beams;
    private CopyOnWriteArrayList<String> recentUris;
    @Inject
    Bulp bulp;

    @PostConstruct
    public void onInit() {
        this.beams = new CopyOnWriteArrayList<Beam>();
        this.recentUris = new CopyOnWriteArrayList<String>();
    }

    public void onNewRequest(@Observes String uri) {
        this.recentUris.add(uri);
    }

    public void onNewBeam(@Observes Beam beam) {
        this.beams.add(beam);
    }

    @Schedule(minute = "*", hour = "*", second = "*/2")
    public void pulse() {
        for (Beam beam : beams) {
            String message = construct(this.recentUris);
            bulp.flash(beam, message);
            beams.remove(beam);
        }
        this.recentUris.clear();
    }

    String construct(CopyOnWriteArrayList<String> recentUris) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = recentUris.iterator(); it.hasNext();) {
            sb.append(it.next());
            if(it.hasNext()){
                sb.append(',');
            }
        }
        return sb.toString();
    }
}

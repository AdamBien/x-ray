package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.HitsPer;
import com.abien.xray.business.statistics.entity.Statistic;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.AccessTimeout;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.abien.xray.business.HitsPer.Frequency.*;


/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("trend")
@Singleton
@AccessTimeout(2000)
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class Trend {

    private LinkedList<Statistic> hourly = new LinkedList<Statistic>();
    private LinkedList<Statistic> minutely = new LinkedList<Statistic>();

    @GET
    @Path("hourly")
    @Lock(LockType.READ)
    public List<Statistic> hourly(@QueryParam("lastHours") @DefaultValue("24") int hours) {
        int size = hourly.size();
        if (size <= hours) {
            return hourly;
        }
        return hourly.subList(0, hours);
    }

    @GET
    @Path("minutely")
    @Lock(LockType.READ)
    public List<Statistic> minutely(@QueryParam("lastHours") @DefaultValue("60") int minutes) {
        int size = minutely.size();
        if (size <= minutes) {
            return minutely;
        }
        return minutely.subList(0, minutes);
    }

    @Lock(LockType.WRITE)
    public void onHourlyStatistic(@Observes @HitsPer(HOUR) long hitPerMinute) {
        hourly.addFirst(new Statistic(hitPerMinute));
    }

    @Lock(LockType.WRITE)
    public void onMinutelyStatistic(@Observes @HitsPer(MINUTE) long hitPerMinute) {
        minutely.addFirst(new Statistic(hitPerMinute));
    }
}

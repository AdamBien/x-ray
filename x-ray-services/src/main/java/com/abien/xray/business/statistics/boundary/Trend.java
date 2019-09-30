package com.abien.xray.business.statistics.boundary;

import com.abien.xray.business.HitsPer;
import static com.abien.xray.business.HitsPer.Frequency.*;
import com.abien.xray.business.statistics.entity.Statistic;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/**
 * @author Adam Bien, blog.adam-bien.com
 */
@ApplicationScoped
@Path("trend")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class Trend {

    private LinkedList<Statistic> hourly = new LinkedList<Statistic>();
    private LinkedList<Statistic> minutely = new LinkedList<Statistic>();

    @GET
    @Path("hourly")
    public List<Statistic> hourly(@QueryParam("lastHours") @DefaultValue("24") int hours) {
        int size = hourly.size();
        if (size <= hours) {
            return hourly;
        }
        return hourly.subList(0, hours);
    }

    @GET
    @Path("minutely")
    public List<Statistic> minutely(@QueryParam("lastHours") @DefaultValue("60") int minutes) {
        int size = minutely.size();
        if (size <= minutes) {
            return minutely;
        }
        return minutely.subList(0, minutes);
    }

    public void onHourlyStatistic(@Observes @HitsPer(HOUR) long hitPerMinute) {
        hourly.addFirst(new Statistic(hitPerMinute));
    }

    public void onMinutelyStatistic(@Observes @HitsPer(MINUTE) long hitPerMinute) {
        minutely.addFirst(new Statistic(hitPerMinute));
    }
}

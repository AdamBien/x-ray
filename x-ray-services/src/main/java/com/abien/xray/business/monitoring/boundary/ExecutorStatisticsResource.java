package com.abien.xray.business.monitoring.boundary;

import com.airhacks.porcupine.execution.entity.Statistics;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Path("executor-statistics")
public class ExecutorStatisticsResource {

    @Inject
    Instance<List<Statistics>> statistics;

    @GET
    public List<Statistics> all() {
        return statistics.get();
    }

}

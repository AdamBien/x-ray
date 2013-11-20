package com.abien.xray.business.useragent.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.useragent.control.UserAgentStatistics;
import com.abien.xray.business.useragent.entity.UserAgent;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * User: blog.adam-bien.com Date: 07.01.11 Time: 13:39
 */
@Path("useragents")
@Stateless
@Interceptors(PerformanceAuditor.class)
public class UserAgentStatisticsResource {

    @Inject
    UserAgentStatistics agentStatistics;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserAgent> topUserAgents(@QueryParam("max") @DefaultValue("50") int maxNumber) {
        return agentStatistics.getMostPopularAgents(maxNumber);
    }
}

package com.abien.xray.business.useragent.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.useragent.control.UserAgentStatistics;
import com.abien.xray.business.useragent.entity.UserAgent;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * User: blog.adam-bien.com
 * Date: 07.01.11
 * Time: 13:39
 */
@Path("useragents")
@Stateless
@Interceptors(PerformanceAuditor.class)
public class UserAgentStatisticsResource {

    @EJB
    UserAgentStatistics agentStatistics;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<UserAgent> topUserAgents(@QueryParam("max") @DefaultValue("50") int maxNumber) {
            return agentStatistics.getMostPopularAgents(maxNumber);
    }
}

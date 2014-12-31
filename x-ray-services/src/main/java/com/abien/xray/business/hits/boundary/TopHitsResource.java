package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.hits.entity.Hit;
import com.abien.xray.business.hits.entity.Post;
import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author blog.adam-bien.com
 */
@Path("mostpopular")
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Interceptors(PerformanceAuditor.class)
public class TopHitsResource extends TitleFilter {

    @Inject
    HitsManagement hits;

    @Inject
    XRayLogger LOG;

    private List<Post> recentPostsWithExistingTitle;

    @PostConstruct
    public void initialize() {
        this.recentPostsWithExistingTitle = new ArrayList<>();
    }

    @GET
    @Produces({MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void totalHitsAsString(@Suspended AsyncResponse asyncResponse, @QueryParam("max") @DefaultValue("10") int max) {
        asyncResponse.setTimeout(80, TimeUnit.MILLISECONDS);
        asyncResponse.setTimeoutHandler(this::sendPreviousValues);
        List<Hit> mostPopularPostsWithoutTitle = hits.getMostPopularPosts(max);
        LOG.log(Level.INFO, "Got hit list: " + mostPopularPostsWithoutTitle);
        List<Post> collect = mostPopularPostsWithoutTitle.stream().map(this::convert).collect(Collectors.toList());
        this.recentPostsWithExistingTitle = getPostsWithExistingTitle(collect, max);
        sendPreviousValues(asyncResponse);
    }

    void sendPreviousValues(AsyncResponse response) {
        response.resume(this.recentPostsWithExistingTitle);
    }

    Post convert(Hit hit) {
        long count = hit.getCount();
        String uri = hit.getActionId();
        return new Post(uri, count);
    }
}

package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.entity.Post;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import java.util.List;
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
import javax.ws.rs.core.MediaType;

/**
 *
 * @author blog.adam-bien.com
 */
@Path("mostpopular")
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Interceptors(PerformanceAuditor.class)
public class MostPopularPostsResource {

    @Inject
    MostPopularPostsFetcher fetcher;

    @GET
    @Produces({MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Post> totalHitsAsString(@QueryParam("max") @DefaultValue("10") int max) {
        return this.fetcher.getMostPopularPosts(max);
    }
}

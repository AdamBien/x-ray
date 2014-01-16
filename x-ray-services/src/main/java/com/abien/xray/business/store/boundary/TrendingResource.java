package com.abien.xray.business.store.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.control.HitsManagement;
import com.abien.xray.business.store.entity.Post;
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
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("trending")
@Stateless
@Interceptors(PerformanceAuditor.class)
public class TrendingResource extends TitleFilter {

    @Inject
    HitsManagement hits;

    @GET
    @Produces({MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Post> getTrendingPostsWithTitle(@QueryParam("max") @DefaultValue("10") int max) {
        List<Post> mostPopularPosts = hits.getTrending();
        return getPostsWithExistingTitle(mostPopularPosts, max);
    }
}

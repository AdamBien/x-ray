package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.hits.entity.Post;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
@RequestScoped
@Path("trending")
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

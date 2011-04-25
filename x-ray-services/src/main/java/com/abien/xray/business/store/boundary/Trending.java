package com.abien.xray.business.store.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.control.TitleFetcher;
import com.abien.xray.business.store.entity.Hit;
import com.abien.xray.business.store.entity.Post;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import javax.interceptor.Interceptors;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("trending")
@Stateless
@Interceptors(PerformanceAuditor.class)
public class Trending extends TitleFilter{

    @EJB
    Hits hits;


    @GET
    @Produces({MediaType.APPLICATION_XHTML_XML,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<Post> getTrendingPostsWithTitle(@QueryParam("max") @DefaultValue("10") int max){
        List<Post> mostPopularPosts = hits.getTrending();
        return getPostsWithExistingTitle(mostPopularPosts,max);
    }
}

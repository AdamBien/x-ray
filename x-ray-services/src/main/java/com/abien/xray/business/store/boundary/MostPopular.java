package com.abien.xray.business.store.boundary;

import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.control.PersistentHitStore;
import com.abien.xray.business.store.entity.Hit;
import com.abien.xray.business.store.entity.Post;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Stateless
@Interceptors(PerformanceAuditor.class)
public class MostPopular extends TitleFilter{

    @EJB
    PersistentHitStore hits;


    @GET
    @Produces({MediaType.APPLICATION_XHTML_XML,MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public List<Post> totalHitsAsString(@QueryParam("max") @DefaultValue("10") int max){
        List<Post> mostPopularPosts = new LinkedList<Post>();
        List<Hit> mostPopularPostsWithoutTitle = hits.getMostPopularPosts(max);
        for (Hit hit : mostPopularPostsWithoutTitle) {
            mostPopularPosts.add(convert(hit));
        }
        return getPostsWithExistingTitle(mostPopularPosts,max);
    }

    Post convert(Hit hit){
        long count = hit.getCount();
        String uri = hit.getActionId();
        return new Post(uri, count);
    }
}

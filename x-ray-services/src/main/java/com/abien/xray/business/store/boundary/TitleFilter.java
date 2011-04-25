package com.abien.xray.business.store.boundary;

import com.abien.xray.business.store.control.TitleFetcher;
import com.abien.xray.business.store.entity.Post;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author Adam Bien, blog.adam-bien.com
 */
public class TitleFilter {

    @EJB
    TitleFetcher cache;

    List<Post> getPostsWithExistingTitle(List<Post> mostPopularPosts, int max) {
        List<Post> mostPopularPostsWithTitle = new ArrayList<Post>();
        for (Post post : mostPopularPosts) {
            if(isBogus(post)){
                continue;
            }
            fetchTitle(post);
            if (!post.isTitleEmpty()) {
                mostPopularPostsWithTitle.add(post);
            }
        }
        return trim(mostPopularPostsWithTitle, max);
    }

    boolean isBogus(Post post) {
        return this.cache.isBogus(post.getUri());
    }
    
    void fetchTitle(Post post) {
        String uri = post.getUri();
        String title = cache.getTitle(uri);
        post.setTitle(title);
    }

    List<Post> trim(List<Post> posts, int max) {
        int listSize = posts.size();
        if (listSize > max) {
            return posts.subList(0, max);
        }
        return posts;

    }

}

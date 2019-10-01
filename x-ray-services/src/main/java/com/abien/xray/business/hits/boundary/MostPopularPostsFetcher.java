package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.cache.boundary.ResultCache;
import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.hits.entity.Hit;
import com.abien.xray.business.hits.entity.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Dependent
public class MostPopularPostsFetcher extends TitleFilter {

    @Inject
    HitsManagement hits;

    @Inject
    ResultCache<List<Post>> mostPopularPostsCache;

    public List<Post> computeMostPopularPosts(int max) {
        List<Hit> mostPopularPostsWithoutTitle = hits.getMostPopularPosts(max);
        List<Post> collect = mostPopularPostsWithoutTitle.stream().map(this::convert).collect(Collectors.toList());
        return getPostsWithExistingTitle(collect, max);
    }

    public List<Post> getMostPopularPosts(int max) {
        return this.mostPopularPostsCache.getCachedValueOr(() -> computeMostPopularPosts(max), new ArrayList<>());
    }

    Post convert(Hit hit) {
        long count = hit.getCount();
        String uri = hit.getActionId();
        return new Post(uri, count);
    }

}

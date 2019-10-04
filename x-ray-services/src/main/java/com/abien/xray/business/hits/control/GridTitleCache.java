package com.abien.xray.business.hits.control;

import static com.abien.xray.business.hits.entity.Post.EMPTY;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
@ApplicationScoped
public class GridTitleCache {

    Map<String, String> titles;

    @PostConstruct
    public void initCache() {
        this.titles = new ConcurrentHashMap<>();
    }


    public String get(String uri) {
        return this.titles.get(uri);
    }

    public void put(String uri, String title) {
        titles.put(uri, title);
    }

    public List<String> getURIsWithoutTitle() {
        return StreamSupport.stream(this.titles.entrySet().spliterator(), false).
                filter(entry -> EMPTY.equals(entry.getValue())).
                map(e -> e.getKey()).
                collect(Collectors.toList());
    }

    public void remove(String uri) {
        this.titles.remove(uri);
    }

}

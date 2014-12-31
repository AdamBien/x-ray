package com.abien.xray.business.hits.control;

import static com.abien.xray.business.hits.entity.Post.EMPTY;
import com.airhacks.xray.grid.control.Grid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.cache.Cache;
import javax.inject.Inject;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class GridTitleCache {

    @Inject
    @Grid(Grid.Name.TITLES)
    private Cache<String, String> titles;

    public String get(String uri) {
        return this.titles.get(uri);
    }

    public void put(String uri, String title) {
        titles.put(uri, title);
    }

    public List<String> getURIsWithoutTitle() {
        return StreamSupport.stream(this.titles.spliterator(), false).
                filter(entry -> EMPTY.equals(entry.getValue())).
                map(e -> e.getKey()).
                collect(Collectors.toList());
    }

    public void remove(String uri) {
        this.titles.remove(uri);
    }

}

package com.abien.xray.business.store.control;

import com.abien.xray.business.grid.control.Titles;
import static com.abien.xray.business.store.entity.Post.EMPTY;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class GridTitleCache {

    @Inject
    @Titles
    private ConcurrentMap<String, String> titles = null;

    public String get(String uri) {
        return this.titles.get(uri);
    }

    public void put(String uri, String title) {
        titles.put(uri, title);
    }

    public List<String> getURIsWithoutTitle() {
        List<String> uris = new ArrayList<String>();
        Set<Entry<String, String>> titleSet = this.titles.entrySet();
        for (Entry<String, String> entry : titleSet) {
            if (EMPTY.equals(entry.getValue())) {
                String key = entry.getKey();
                uris.add(key);
            }
        }
        return uris;
    }

    public void remove(String uri) {
        this.titles.remove(uri);
    }

}

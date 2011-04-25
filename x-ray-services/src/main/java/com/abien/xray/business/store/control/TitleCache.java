package com.abien.xray.business.store.control;

import java.util.List;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public interface TitleCache {

    String get(String uri);
    void put(String uri, String title);
    void remove(String uri);
    List<String> getURIsWithoutTitle();


}

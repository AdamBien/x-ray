package com.abien.xray.business.store.control;

import java.util.Map.Entry;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import static com.abien.xray.business.store.entity.Post.EMPTY;

/**
 *
 * @author Adam Bien <blog.adam-bien.com>
 */
public class InMemoryTitleCache implements TitleCache {
    
   private ConcurrentHashMap<String, String> titles = null;

   @PostConstruct
   public void initialize(){
       this.titles = new ConcurrentHashMap<String, String>();
   }
   
   public String get(String uri){
       return this.titles.get(uri);
   }

    @Override
    public void put(String uri, String title) {
        titles.put(uri, title);
    }

    @Override
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

    @Override
    public void remove(String uri) {
        this.titles.remove(uri);
    }

}

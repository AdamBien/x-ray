package com.abien.xray.business.store.control;

import com.abien.xray.business.logging.boundary.XRayLogger;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import static com.abien.xray.business.store.entity.Post.EMPTY;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Singleton
@AccessTimeout(value = 20, unit = TimeUnit.SECONDS)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TitleFetcher {

    @Inject
    XRayLogger LOG;

    private ConcurrentLinkedQueue<String> bogusTitles = null;
    public static final String BASE_URL = "http://www.adam-bien.com/roller/abien";

    @EJB
    PersistentHitStore persistentHitStore;
    @Inject
    TitleCache titles;

    @PostConstruct
    public void initialize() {
        this.bogusTitles = new ConcurrentLinkedQueue<String>();
    }

    public String getTitle(String uri) {
        if (isBogus(uri)) {
            return EMPTY;
        }
        String title = titles.get(uri);
        if (title == null) {
            title = EMPTY;
            titles.put(uri, title);
        }
        return title;
    }

    @Schedule(minute = "*/1", hour = "*", persistent = false)
    public void fetchTitles() {
        List<String> emptyUris = this.titles.getURIsWithoutTitle();
        for (String key : emptyUris) {
            String value = fetchTitle(key);
            if (value != null) {
                this.titles.put(key, value);
            }
        }
    }

    public boolean isBogus(String uri) {
        return this.bogusTitles.contains(uri);
    }

    String constructUri(String uriSegment) {
        if (uriSegment == null) {
            return null;
        }
        if (!uriSegment.startsWith("/")) {
            uriSegment = "/" + uriSegment;
        }
        return BASE_URL + uriSegment;
    }

    String extractTitle(String content) {
        if (content == null) {
            return null;
        }
        String start = "<title>";
        String end = "</title>";
        int startIndex = content.indexOf(start);
        if (startIndex == -1) {
            return null;
        }
        int endIndex = content.indexOf(end);
        if (endIndex == -1) {
            return null;
        }
        startIndex += start.length();
        return content.substring(startIndex, endIndex);
    }

    String downloadAndExtractTitle(URL url) throws IOException {
        InputStream stream = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        String line;
        String content = "";
        while ((line = br.readLine()) != null) {
            content += line;
            String extractedTitle = extractTitle(content);
            if (extractedTitle != null) {
                return extractedTitle;
            }
        }
        br.close();
        return null;
    }

    String fetchTitle(String uriSegment) {
        String uri = null;
        try {
            uri = constructUri(uriSegment);
            URL url = new URL(uri);
            return downloadAndExtractTitle(url);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Cannot fetch content from {0} becaue of {1}", new Object[]{uri, ex});
            ignoreURI(uriSegment);
            return null;
        }
    }

    void ignoreURI(String uri) {
        LOG.log(Level.WARNING, "Adding {0} as bogus", new Object[]{uri});
        this.bogusTitles.add(uri);
        this.titles.remove(uri);
        this.persistentHitStore.invalidate(uri);
    }
}

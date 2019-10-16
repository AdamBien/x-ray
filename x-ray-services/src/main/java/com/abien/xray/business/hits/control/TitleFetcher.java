package com.abien.xray.business.hits.control;

import static com.abien.xray.business.hits.control.HitsManagement.HOUR;
import static com.abien.xray.business.hits.entity.Post.EMPTY;
import com.abien.xray.business.logging.boundary.XRayLogger;
import io.quarkus.scheduler.Scheduled;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@ApplicationScoped
public class TitleFetcher {

    @Inject
    XRayLogger LOG;

    private ConcurrentLinkedQueue<String> bogusTitles = null;
    public static final String BASE_URL = "http://www.adam-bien.com/roller/abien";

    @Inject
    GridTitleCache titles;

    @PostConstruct
    public void initialize() {
        this.bogusTitles = new ConcurrentLinkedQueue<>();
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

    @Scheduled(every = HOUR)
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
    }
}

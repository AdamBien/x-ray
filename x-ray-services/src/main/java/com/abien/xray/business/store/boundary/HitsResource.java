package com.abien.xray.business.store.boundary;

import com.abien.xray.business.logging.boundary.XRayLogger;
import com.abien.xray.business.monitoring.PerformanceAuditor;
import com.abien.xray.business.store.control.HitsManagement;
import com.abien.xray.business.store.control.URLPathExtractor;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@Path("hits")
@Stateless
@Interceptors(PerformanceAuditor.class)
public class HitsResource {

    @Inject
    private XRayLogger LOG;

    @Inject
    URLPathExtractor extractor;
    @Inject
    HitsManagement hits;

    public static final String PREFIX = "/entry/";

    @PUT
    @Consumes({MediaType.TEXT_PLAIN})
    public Response updateStatistics(@Context HttpHeaders httpHeaders, String url) {
        if (!isEmpty(url)) {
            MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
            Map<String, String> headerMap = new HashMap<>();
            for (Map.Entry<String, List<String>> headerEntries : headers.entrySet()) {
                String headerName = headerEntries.getKey();
                List<String> headerValuesList = headerEntries.getValue();
                if (headerValuesList != null && !headerValuesList.isEmpty()) {
                    String headerValue = headerValuesList.get(0);
                    headerMap.put(headerName, headerValue);
                }
            }
            processURL(url, headerMap);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("{uri}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hitsForURI(@PathParam("uri") String uri) {
        LOG.log(Level.INFO, "hitsForURI with {0}", new Object[]{uri});
        String decoded;
        try {
            decoded = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "--";
        }
        String decodedURIWithPrefix = prependPrefix(decoded);
        LOG.log(Level.INFO, "{0} decoded {1}", new Object[]{uri, decodedURIWithPrefix});
        String nbrOfHits = String.valueOf(hits.getHitsForURI(decodedURIWithPrefix));
        LOG.log(Level.INFO, "returning {0} for {1}", new Object[]{uri, nbrOfHits});
        return nbrOfHits;
    }

    String prependPrefix(String post) {
        if (post == null) {
            return null;
        }
        if (post.startsWith(PREFIX)) {
            return post;
        } else {
            return PREFIX + removeLeadingSlash(post);
        }

    }

    String removeLeadingSlash(String post) {
        if (!post.startsWith("/")) {
            return post;
        } else {
            return post.substring(1, post.length());
        }
    }

    void processURL(String url, Map<String, String> headerMap) {
        String uniqueAction = extractor.extractPathSegmentFromURL(url);
        LOG.log(Level.INFO, "updateStatistics({0}) - extracted uniqueAction: {1}", new Object[]{url, uniqueAction});
        String referer = extractor.extractReferer(url);
        LOG.log(Level.INFO, "updateStatistics({0}) - extracted referer: {1}", new Object[]{url, referer});
        hits.updateStatistics(uniqueAction, referer, headerMap);
    }

    boolean isEmpty(String url) {
        if (url == null) {
            return true;
        }
        String trimmed = url.trim();
        return trimmed.isEmpty();
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String totalHitsAsString() {
        return hits.totalHitsAsString();
    }
}

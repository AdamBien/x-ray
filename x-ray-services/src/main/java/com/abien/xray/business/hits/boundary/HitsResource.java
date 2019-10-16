package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import com.abien.xray.business.hits.control.InboundProcessor;
import com.abien.xray.business.hits.control.JsonSerializer;
import com.abien.xray.business.logging.boundary.XRayLogger;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
import javax.ws.rs.core.Response.Status;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

/**
 * @author Adam Bien, blog.adam-bien.com
 */
@ApplicationScoped
@Path("hits")
public class HitsResource {

    @Inject
    XRayLogger LOG;

    @Inject
    HitsManagement hits;

    @Inject
    InboundProcessor ip;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;


    public static final String PREFIX = "/entry/";

    @PUT
    @Metered
    @Consumes({MediaType.TEXT_PLAIN})
    public Response updateStatistics(@Context HttpHeaders httpHeaders, String encodedUrl) {
        String url = null;
        if (!isEmpty(encodedUrl)) {
            try {
                url = URLDecoder.decode(encodedUrl, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                registry.counter("encoding_error").inc();
                return Response.status(Status.BAD_REQUEST).header("Encoding problems:", e.toString()).build();
            }
            MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
            JsonObjectBuilder request = Json.createObjectBuilder();
            headers.entrySet().stream().forEach((headerEntries) -> {
                String headerName = headerEntries.getKey();
                List<String> headerValuesList = headerEntries.getValue();
                if (headerValuesList != null && !headerValuesList.isEmpty()) {
                    String headerValue = headerValuesList.get(0);
                    request.add(headerName, headerValue);
                }
            });
            request.add("url", url);
            processRequest(request.build());
            registry.counter("request_processed").inc();
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

    void processRequest(JsonObject object) {
        String serialized = JsonSerializer.serialize(object);
        ip.processURL(serialized);
    }

}

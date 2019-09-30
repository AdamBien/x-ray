package com.abien.xray.business.versioning.boundary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author adam-bien.com
 */
@RequestScoped
@Path("version")
@Produces(MediaType.TEXT_PLAIN)
public class VersionResource {

    public static final Logger LOG = Logger.getLogger(VersionResource.class.getName());

    @GET
    public JsonObject version() {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        Properties properties = new Properties();
        InputStream is = null; //this.context.getResourceAsStream("/META-INF/maven/com.airhacks/x-ray-services/pom.properties");

        if (is != null) {
            try {
                properties.load(is);
                Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
                entrySet.stream().forEach((entry) -> {
                    objectBuilder.add(entry.getKey().toString(), entry.getValue().toString());
                });
            } catch (IOException ex) {
                throw new WebApplicationException("Cannot load properties. Reason: " + ex, Response.Status.NOT_FOUND);

            }
        }
        return objectBuilder.build();
    }
}

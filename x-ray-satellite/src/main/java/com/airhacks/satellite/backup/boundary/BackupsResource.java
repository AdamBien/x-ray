package com.airhacks.satellite.backup.boundary;

import com.airhacks.satellite.backup.UnknownCacheException;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author airhacks.com
 */
@Stateless
@Path("backups")
public class BackupsResource {

    @Inject
    Backup sp;

    @GET
    public JsonObject mapCaches(@Context UriInfo info) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        URI path = info.getAbsolutePath();

        Set<String> mapCaches = sp.mapCaches();
        mapCaches.forEach(name
                -> objectBuilder.add(name, UriBuilder.fromUri(path).path(name).build().getPath())
        );
        return objectBuilder.build();
    }

    @GET
    @Path("{name}")
    public Response mapBackup(@PathParam("name") String name, @Context HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        try {
            sp.writeMapToStream(name, outputStream);
        } catch (UnknownCacheException ex) {
            return Response.status(Response.Status.NOT_FOUND).header("x-error", ex.getMessage()).build();
        }
        return Response.ok().build();
    }
}

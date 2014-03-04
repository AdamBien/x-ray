package com.airhacks.satellite.backup.boundary;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author airhacks.com
 */
@Stateless
@Path("backups")
public class BackupsResource {

    @Inject
    Backup backup;

    @GET
    public JsonObject mapCaches(@Context UriInfo info) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        URI absolute = info.getAbsolutePath();
        Set<String> mapCaches = backup.mapCaches();
        mapCaches.forEach(name
                -> objectBuilder.add(name, UriBuilder.
                        fromUri(absolute).path(name).build().getPath())
        );
        return objectBuilder.build();
    }

    @GET
    @Path("{name}")
    public Response download(@PathParam("name") String name) throws IOException {
        if (!backup.cacheExists(name)) {
            return Response.status(Response.Status.OK).
                    header("x-error", "Cache with name: " + name + " does not exist!").
                    build();
        }
        int backupSize = backup.size(name);
        StreamingOutput output = outputStream -> {
            backup.writeMapToStream(name, outputStream);
            outputStream.flush();
            outputStream.close();
        };

        return Response.ok(output).header("x-number-of-objects", backupSize).build();
    }

    @PUT
    @Path("{name}")
    public Response restore(@PathParam("name") String name, Reader reader) {
        int restoredObjects = this.backup.store(name, reader);
        return Response.ok().header("x-number-of-objects", restoredObjects).build();
    }
}

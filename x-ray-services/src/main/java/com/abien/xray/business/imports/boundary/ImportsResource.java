
package com.abien.xray.business.imports.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
@Path("imports")
public class ImportsResource {

    @Inject
    HitsManagement management;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    @PUT
    @Path("hits")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(JsonObject input) {
        String id = prependEntry(input.getString("id"));
        String hits = input.getString("hits");
        if (!checkNumber(hits)) {
            registry.counter("conversion_errors").inc();
            return Response.status(400).header("reason", hits + " is not a number").build();
        }
        registry.counter("imported_hits").inc();
        long newHitsValue = this.management.updateHitsForURI(id, Long.parseLong(hits));
        return Response.ok(newHitsValue).build();
    }

    boolean checkNumber(String numberCandidate) {
        try {
            Long.parseLong(numberCandidate);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    String prependEntry(String id) {
        if (!id.startsWith("/entry")) {
            return "/entry/" + id;
        }
        return id;
    }

}

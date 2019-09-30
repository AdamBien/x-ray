
package com.abien.xray.business.hits.boundary;

import com.abien.xray.business.hits.control.HitsManagement;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author airhacks.com
 */
@RequestScoped
@Path("imports")
public class ImportsResource {

    @Inject
    HitsManagement management;

    @PUT
    @Path("hits")
    public Response update(JsonObject input) {
        String id = input.getString("id");
        String hits = input.getString("hits");
        if (!checkNumber(hits)) {
            return Response.status(400).header("reason", hits + " is not a number").build();
        }
        this.management.updateHitsForURI(id, hits);
        return Response.noContent().build();
    }

    boolean checkNumber(String numberCandidate) {
        try {
            Long.parseLong(numberCandidate);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}

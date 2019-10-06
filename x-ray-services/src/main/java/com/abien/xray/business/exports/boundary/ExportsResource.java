package com.abien.xray.business.exports.boundary;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.stream.JsonCollectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.abien.xray.business.hits.control.HitsManagement;



@Produces(MediaType.APPLICATION_JSON)
@Path("exports")
public class ExportsResource {


    @Inject
    HitsManagement management;

    @GET
    public JsonArray all() {
        return this.management.getHits().entrySet().stream().map(e -> Json.createObjectBuilder().add(e.getKey(),e.getValue()).build()).collect(JsonCollectors.toJsonArray());
    }



    
}
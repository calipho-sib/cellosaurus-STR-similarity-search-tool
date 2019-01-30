package org.expasy.cellosaurus.resources;

import com.google.gson.Gson;
import org.expasy.cellosaurus.Manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/database")
public class DatabaseResource {

    @GET
    @Produces("application/json")
    public Response get(@Context UriInfo info) {
        Gson gson = new Gson();
        String json = gson.toJson(Manager.database);

        return Response
                .status(200)
                .entity(json)
                .build();
    }
}

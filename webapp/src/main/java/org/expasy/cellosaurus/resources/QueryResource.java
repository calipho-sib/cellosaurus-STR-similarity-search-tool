package org.expasy.cellosaurus.resources;

import org.expasy.cellosaurus.Manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/query")
public class QueryResource {

    @GET
    @Produces("application/json")
    public Response get(@Context UriInfo info) {
        String json = Manager.search(info.getQueryParameters());

        return Response
                .status(200)
                .entity(json)
                .build();
    }
}

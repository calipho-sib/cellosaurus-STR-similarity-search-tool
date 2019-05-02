package org.expasy.cellosaurus.resources;

import com.google.gson.Gson;
import org.expasy.cellosaurus.Manager;
import org.glassfish.jersey.internal.util.ExceptionUtils;

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
        try {
            Gson gson = new Gson();

            return Response
                    .status(200)
                    .entity(gson.toJson(Manager.database))
                    .build();

        } catch (Exception e) {
            return Response
                    .status(500)
                    .entity(ExceptionUtils.exceptionStackTraceAsString(e))
                    .type("text/plain")
                    .build();
        }
    }
}

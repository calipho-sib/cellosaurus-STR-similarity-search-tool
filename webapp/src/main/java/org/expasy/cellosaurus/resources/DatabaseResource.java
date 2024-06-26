package org.expasy.cellosaurus.resources;

import org.expasy.cellosaurus.db.Database;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Class representing the database API resource. Its GET HTTP method is used to report on the version of the Cellosaurus
 * database that the STR Similarity Search Tool is using.
 */
@Path("/database")
public class DatabaseResource {

    /**
     * Return the information about the Cellosaurus release version in use
     *
     * @param info the URI information
     * @return the HTTP {@code Response}
     */
    @GET
    @Produces("application/json")
    public Response get(@Context UriInfo info) {
        try {
            return Response
                    .status(200)
                    .entity(Database.CELLOSAURUS.toJson())
                    .type("application/json")
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

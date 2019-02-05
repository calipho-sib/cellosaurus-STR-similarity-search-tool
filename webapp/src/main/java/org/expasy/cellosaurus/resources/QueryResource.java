package org.expasy.cellosaurus.resources;

import org.expasy.cellosaurus.Manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/query")
public class QueryResource {

    @GET
    public Response get(@Context UriInfo info) {
        MultivaluedMap<String, String> map = info.getQueryParameters();

        String type = "application/json";
        String content = "inline";

        if (map.containsKey("format")) {
            if (map.getFirst("format").equalsIgnoreCase("csv")) {
                type = "text/csv";
                content = "attachment; filename=Cellosaurus_STR_Results.csv";
            }
        }
        String answer = Manager.search(map, type);

        return Response
                .status(200)
                .entity(answer)
                .type(type)
                .header("Content-Disposition", content)
                .build();
    }
}

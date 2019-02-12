package org.expasy.cellosaurus.resources;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.Manager;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("/query")
public class QueryResource {

    @GET
    @Produces({"application/json", "text/csv"})
    public Response get(@Context UriInfo info) {
        try {
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

        } catch (IllegalArgumentException e) {
            return Response
                    .status(400)
                    .entity(e.toString())
                    .type("text/plain")
                    .build();
        }
    }

    @POST
    @Consumes("application/json")
    @Produces({"application/json", "text/csv"})
    public Response post(String input) {
        try {
            MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

            String type = "application/json";
            String content = "inline";

            JsonObject object = new JsonParser().parse(input).getAsJsonObject();
            for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
                map.add(elements.getKey(), elements.getValue().getAsString());

                if (elements.getKey().equalsIgnoreCase("format")) {
                    if (elements.getValue().getAsString().equalsIgnoreCase("csv")) {
                        type = "text/csv";
                        content = "attachment; filename=Cellosaurus_STR_Results.csv";
                    }
                }
            }
            String answer = Manager.search(map, type);

            return Response
                    .status(200)
                    .entity(answer)
                    .type(type)
                    .header("Content-Disposition", content)
                    .build();

        } catch (IllegalArgumentException e) {
            return Response
                    .status(400)
                    .entity(e.toString())
                    .type("text/plain")
                    .build();
        }
    }
}

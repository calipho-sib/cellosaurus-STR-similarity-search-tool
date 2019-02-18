package org.expasy.cellosaurus.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.format.csv.Formatter;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Map;

@Path("/query")
public class QueryResource {

    @GET
    @Produces({"application/json", "text/csv"})
    public Response get(@Context UriInfo info) {
        MultivaluedMap<String, String> map = info.getQueryParameters();
        String format = "application/json";

        if (map.containsKey("outputFormat")) {
            if (map.getFirst("outputFormat").equalsIgnoreCase("csv")) {
                format = "text/csv";
            }
        }
        return answer(map, format);
    }

    @POST
    @Consumes("application/json")
    @Produces({"application/json", "text/csv"})
    public Response post(String input) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        String format = "application/json";

        JsonObject object = new JsonParser().parse(input).getAsJsonObject();
        for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
            map.add(elements.getKey(), elements.getValue().getAsString());

            if (elements.getKey().equalsIgnoreCase("outputFormat")) {
                if (elements.getValue().getAsString().equalsIgnoreCase("csv")) {
                    format = "text/csv";
                }
            }
        }
        return answer(map, format);
    }

    private Response answer(MultivaluedMap<String, String> map, String format) {
        try {
            String answer;
            String disposition;

            if (format.equals("application/json")) {
                Gson gson = new Gson();
                answer = gson.toJson(Manager.search(map));
                disposition = "inline";
            } else {
                Formatter formatter = new Formatter();
                answer = formatter.toCsv(Manager.search(map));
                disposition = "attachment; filename=Cellosaurus_STR_Results.csv";
            }

            return Response
                    .status(200)
                    .entity(answer)
                    .type(format)
                    .header("Content-Disposition", disposition)
                    .build();

        } catch (IllegalArgumentException e) {
            Gson gson = new Gson();

            return Response
                    .status(400)
                    .entity(gson.toJson(e.toString()))
                    .type("application/json")
                    .build();
        }
    }
}

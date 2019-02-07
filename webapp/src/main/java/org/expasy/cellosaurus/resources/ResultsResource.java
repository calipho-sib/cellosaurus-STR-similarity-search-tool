package org.expasy.cellosaurus.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.Manager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

@Path("/results")
public class ResultsResource {

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public String post(String input) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        JsonObject object = new JsonParser().parse(input).getAsJsonObject();
        for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
            map.add(elements.getKey(), elements.getValue().getAsString());
        }

        return Manager.search(map, "application/json");
    }
}

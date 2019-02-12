package org.expasy.cellosaurus.resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.format.zip.Writer;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Path("/batch")
public class BatchResource {

    @POST
    @Consumes("application/json")
    @Produces("application/zip")
    public Response post(String input) {
        try {
            Writer writer = new Writer();

            JsonArray array = new JsonParser().parse(input).getAsJsonArray();
            for (int i = 0; i < array.size()-1; i++) {
                JsonObject object = array.get(i).getAsJsonObject();

                MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
                for (Map.Entry<String, JsonElement> elements : array.get(array.size()-1).getAsJsonObject().entrySet()) {
                    map.add(elements.getKey(), elements.getValue().getAsString());
                }
                for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
                    map.add(elements.getKey(), elements.getValue().getAsString());
                }
                writer.add(object.get("SampleReferenceNbr").getAsString(), Manager.search(map, "test/csv"));
            }
            writer.write();
            byte[] answer = Files.readAllBytes(writer.getZip().toPath());
            writer.close();

            return Response
                    .status(200)
                    .entity(answer)
                    .type("application/zip")
                    .header("Content-Disposition", "filename=Cellosaurus_STR_Results.zip")
                    .build();

        } catch (IllegalArgumentException e) {
            return Response
                    .status(400)
                    .entity(e.toString())
                    .type("text/plain")
                    .build();

        } catch (IOException e) {
            return Response
                    .status(500)
                    .entity(e.toString())
                    .type("text/plain")
                    .build();
        }
    }
}

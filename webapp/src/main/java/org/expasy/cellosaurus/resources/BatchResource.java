package org.expasy.cellosaurus.resources;

import com.google.gson.*;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.formats.csv.CsvFormatter;
import org.expasy.cellosaurus.formats.zip.ZipWriter;
import org.expasy.cellosaurus.wrappers.Search;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/batch")
public class BatchResource {

    @POST
    @Consumes("application/json")
    @Produces({"application/zip", "application/json"})
    public Response post(String input) {
        try {
            ZipWriter zipWriter = new ZipWriter();
            CsvFormatter csvFormatter = new CsvFormatter();

            boolean isJson = true;
            JsonArray jsonArray = new JsonParser().parse(input).getAsJsonArray();
            for (Map.Entry<String, JsonElement> elements : jsonArray.get(jsonArray.size() - 1).getAsJsonObject().entrySet()) {
                if (elements.getKey().equalsIgnoreCase("outputformat")) {
                    isJson = elements.getValue().getAsString().equalsIgnoreCase("json");
                    break;
                }
            }

            List<Search> searches = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject object = jsonArray.get(i).getAsJsonObject();

                MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
                for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
                    map.add(elements.getKey().toUpperCase(), elements.getValue().getAsString());
                }
                if (!map.containsKey("DESCRIPTION")) map.add("DESCRIPTION", "Sample " + (i+1));

                if (isJson) {
                    searches.add(Manager.search(map));
                } else {
                    zipWriter.add(map.getFirst("DESCRIPTION"), csvFormatter.toCsv(Manager.search(map)));
                }
            }
            if (isJson) {
                zipWriter.close();

                Gson gson = new Gson();

                return Response
                        .status(200)
                        .entity(gson.toJson(searches))
                        .type("application/json")
                        .header("Content-Disposition", "inline")
                        .build();
            }

            zipWriter.write();
            byte[] answer = Files.readAllBytes(zipWriter.getZip().toPath());
            zipWriter.close();

            return Response
                    .status(200)
                    .entity(answer)
                    .type("application/zip")
                    .header("Content-Disposition", "filename=Cellosaurus_STR_Results.zip")
                    .build();

        } catch (IllegalArgumentException e) {
            Gson gson = new Gson();

            return Response
                    .status(400)
                    .entity(gson.toJson(e.toString()))
                    .type("application/json")
                    .build();

        } catch (IOException e) {
            Gson gson = new Gson();

            return Response
                    .status(500)
                    .entity(gson.toJson(e.toString()))
                    .type("application/json")
                    .build();
        }
    }
}

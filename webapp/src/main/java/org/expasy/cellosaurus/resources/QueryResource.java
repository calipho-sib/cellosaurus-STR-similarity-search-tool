package org.expasy.cellosaurus.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.formats.FormatsUtils;
import org.expasy.cellosaurus.formats.csv.CsvFormatter;
import org.expasy.cellosaurus.formats.xlsx.XlsxWriter;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Path("/query")
public class QueryResource {
    private Gson gson = new Gson();

    @GET
    @Produces({"application/json", "text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public Response get(@Context UriInfo info) {
        MultivaluedMap<String, String> map = info.getQueryParameters();

        String format = "JSON";
        String outputFormat = FormatsUtils.getOutputFormat(map);
        if (!outputFormat.isEmpty()) format = outputFormat;

        return answer(format, map);
    }

    @POST
    @Consumes("application/json")
    @Produces({"application/json", "text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public Response post(String input) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        String format = "JSON";
        JsonObject object = new JsonParser().parse(input).getAsJsonObject();
        for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
            map.add(elements.getKey(), elements.getValue().getAsString());

            String outputFormat = FormatsUtils.getOutputFormat(elements);
            if (!outputFormat.isEmpty()) format = outputFormat;
        }
        return answer(format, map);
    }

    private Response answer(String format, MultivaluedMap<String, String> map) {
        try {
            if (format.equals("JSON")) {
                return Response
                        .status(200)
                        .entity(gson.toJson(Manager.search(map)))
                        .type("application/json")
                        .header("Content-Disposition", "inline")
                        .build();

            } else if (format.equals("CSV")){
                CsvFormatter csvFormatter = new CsvFormatter();
                return Response
                        .status(200)
                        .entity(csvFormatter.toCsv(Manager.search(map)))
                        .type("text/csv")
                        .header("Content-Disposition", "attachment; filename=Cellosaurus_STR_Results.csv")
                        .build();
            } else {
                XlsxWriter xlsxWriter = new XlsxWriter();
                xlsxWriter.add(Manager.search(map));
                xlsxWriter.write();
                byte[] answer = Files.readAllBytes(xlsxWriter.getXlsx().toPath());
                xlsxWriter.close();

                return Response
                        .status(200)
                        .entity(answer)
                        .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .header("Content-Disposition", "attachment; filename=Cellosaurus_STR_Results.xlsx")
                        .build();
            }
        } catch (IllegalArgumentException e) {
            return Response
                    .status(400)
                    .entity(gson.toJson(e.getStackTrace()))
                    .type("application/json")
                    .build();

        } catch (IOException e) {
            return Response
                    .status(500)
                    .entity(gson.toJson(e.getStackTrace()))
                    .type("application/json")
                    .build();
        }
    }
}

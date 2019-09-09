package org.expasy.cellosaurus.resources;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.formats.FormatsUtils;
import org.expasy.cellosaurus.formats.csv.CsvFormatter;
import org.expasy.cellosaurus.formats.xlsx.XlsxWriter;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.nio.file.Files;
import java.util.Map;

/**
 * Class representing the query API resource. Its GET and POST HTTP methods are used to perform a single STR similarity
 * search and return the results in the specified format.
 */
@Path("/query")
public class QueryResource {

    /**
     * Process the submitted query and return the STR similarity search results in the specified format.
     *
     * @param info the URI information
     * @return the HTTP {@code Response}
     */
    @GET
    @Produces({"application/json", "text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public Response get(@Context UriInfo info) {
        MultivaluedMap<String, String> map = info.getQueryParameters();
        String format = FormatsUtils.getFormat(map, "JSON");

        return response(format, map);
    }

    /**
     * Process the submitted query and return the STR similarity search results in the specified format.
     *
     * @param input the input query as a JSON {@code String}
     * @return the HTTP {@code Response}
     */
    @POST
    @Consumes("application/json")
    @Produces({"application/json", "text/csv", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public Response post(String input) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        JsonObject object = new JsonParser().parse(input).getAsJsonObject();
        for (Map.Entry<String, JsonElement> elements : object.entrySet()) {
            map.add(elements.getKey(), elements.getValue().getAsString());
        }
        String format = FormatsUtils.getFormat(object, "JSON");

        return response(format, map);
    }

    /**
     * Process the query map and return the STR similarity search results in the specified format.
     *
     * @param format the selected output format
     * @param map    the query {@code MultivaluedMap} containing the parameter keys and values
     * @return the HTTP {@code Response}
     */
    private Response response(String format, MultivaluedMap<String, String> map) {
        try {
            switch (format) {
                case "JSON":
                    Gson gson = new Gson();

                    return Response
                            .status(200)
                            .entity(gson.toJson(Manager.search(map)))
                            .type("application/json")
                            .header("Content-Disposition", "inline")
                            .build();

                case "XLSX":
                    XlsxWriter xlsxWriter = new XlsxWriter();

                    byte[] answer;
                    try {
                        xlsxWriter.add(Manager.search(map));
                        xlsxWriter.write();
                        answer = Files.readAllBytes(xlsxWriter.getXlsx().toPath());
                    } finally {
                        xlsxWriter.close();
                    }
                    return Response
                            .status(200)
                            .entity(answer)
                            .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .header("Content-Disposition", "attachment; filename=Cellosaurus_STR_Results.xlsx")
                            .build();
                case "CSV":
                    CsvFormatter csvFormatter = new CsvFormatter();

                    return Response
                            .status(200)
                            .entity(csvFormatter.toCsv(Manager.search(map)))
                            .type("text/csv")
                            .header("Content-Disposition", "attachment; filename=Cellosaurus_STR_Results.csv")
                            .build();

                default:
                    throw new IllegalArgumentException("outputFormat=" + format);
            }
        } catch (IllegalArgumentException e) {
            return Response
                    .status(400)
                    .entity(ExceptionUtils.exceptionStackTraceAsString(e))
                    .type("text/plain")
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

package org.expasy.cellosaurus.resources;

import org.expasy.cellosaurus.formats.csv.CsvFormatter;
import org.expasy.cellosaurus.formats.json.JsonFormatter;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Class representing the conversion to CSV API resource. Its POST HTTP method is used to convert the result of a STR
 * similarity search from the JSON to the CSV format.
 */
@Path("/conversion-csv")
public class ConversionCsvResource {

    /**
     * Convert the STR similarity search results from the JSON to the CSV format.
     *
     * @param input the input query as a JSON {@code String}
     * @return the HTTP {@code Response}
     */
    @POST
    @Consumes("application/json")
    @Produces("text/csv")
    public Response post(String input) {
        try {
            JsonFormatter jsonFormatter = new JsonFormatter();
            CsvFormatter csvFormatter = new CsvFormatter();
            return Response
                    .status(200)
                    .entity(csvFormatter.toCsv(jsonFormatter.toSearch(input)))
                    .type("text/csv")
                    .header("Content-Disposition", "attachment; filename=Cellosaurus_STR_Results.csv")
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

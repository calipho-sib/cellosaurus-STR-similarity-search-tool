package org.expasy.cellosaurus.resources;

import com.google.gson.JsonParser;
import org.expasy.cellosaurus.formats.FormatsUtils;
import org.expasy.cellosaurus.formats.csv.CsvFormatter;
import org.expasy.cellosaurus.formats.json.JsonFormatter;
import org.expasy.cellosaurus.formats.xlsx.XlsxWriter;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.nio.file.Files;

/**
 * Class representing the conversion API resource. Its POST HTTP method is used to convert the result of a STR
 * similarity search from the JSON to the XLSX or CSV format.
 */
@Path("/conversion")
public class ConversionResource {

    /**
     * Convert the STR similarity search results from the JSON to the XLSX or CSV format.
     *
     * @param input the input query as a JSON {@code String}
     * @return the HTTP {@code Response}
     */
    @POST
    @Consumes("application/json")
    @Produces({"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/csv"})
    public Response post(String input) {
        try {
            String format = FormatsUtils.getFormat(new JsonParser().parse(input).getAsJsonObject(), "XLSX");
            switch (format) {
                case "XLSX":
                    XlsxWriter xlsxWriter = new XlsxWriter();

                    byte[] xlsxBytes;
                    try {
                        xlsxWriter.add(input);
                        xlsxWriter.write();
                        xlsxBytes = Files.readAllBytes(xlsxWriter.getXlsx().toPath());
                    } finally {
                        xlsxWriter.close();
                    }
                    return Response
                            .status(200)
                            .entity(xlsxBytes)
                            .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .header("Content-Disposition", "filename=Cellosaurus_STR_Results.xlsx")
                            .build();
                case "CSV":
                    JsonFormatter jsonFormatter = new JsonFormatter();
                    CsvFormatter csvFormatter = new CsvFormatter();

                    return Response
                            .status(200)
                            .entity(csvFormatter.toCsv(jsonFormatter.toSearch(input)))
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

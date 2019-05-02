package org.expasy.cellosaurus.resources;

import org.expasy.cellosaurus.formats.xlsx.XlsxWriter;
import org.glassfish.jersey.internal.util.ExceptionUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.nio.file.Files;

@Path("/conversion")
public class ConversionResource {

    @POST
    @Consumes("application/json")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response post(String input) {
        try {
            XlsxWriter xlsxWriter = new XlsxWriter();
            xlsxWriter.add(input);
            xlsxWriter.write();
            byte[] answer = Files.readAllBytes(xlsxWriter.getXlsx().toPath());
            xlsxWriter.close();

            return Response
                    .status(200)
                    .entity(answer)
                    .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "filename=Cellosaurus_STR_Results.xlsx")
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

package org.expasy.cellosaurus.resources;

import org.expasy.cellosaurus.Manager;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

@Path("/results")
public class ResultsResource {

    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    public String post(String input) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        for (String parameter : input.split("&")) {
            String[] parameterArray = parameter.split("=");
            if (parameterArray.length == 1) {
                map.add(parameterArray[0], "");
            } else {
                map.add(parameterArray[0], parameterArray[1]);
            }
        }
        return Manager.search(map);
    }
}

package org.expasy.cellosaurus.formats;

import com.google.gson.JsonElement;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Species;
import org.expasy.cellosaurus.wrappers.Parameters;
import org.expasy.cellosaurus.wrappers.Search;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class storing various static methods used in format conversion and formatting.
 */
public final class FormatsUtils {

    private FormatsUtils() {}

    /**
     * Make the STR markers used for the header of the CSV and XLSX export formats, based on the default core markers
     * and the minor ones contained in the query.
     *
     * @param parameters the search parameters from the query
     * @return the list of STR markers
     */
    public static List<Marker> makeHeaderMarkers(Parameters parameters) {
        List<Marker> headerMarkers = new ArrayList<>();

        for (Marker marker : parameters.getMarkers()) {
            headerMarkers.add(new Marker(marker.getName()));
        }
        Species species = Species.get(parameters.getSpecies());
        for (Marker marker : species.getDefaultMarkers()) {
            if (!headerMarkers.contains(marker)) {
                headerMarkers.add(new Marker(marker));
            }
        }
        Collections.sort(headerMarkers);

        return headerMarkers;
    }

    /**
     * Make the metadata {@code String} used for the header of the CSV and XLSX export formats.
     *
     * @param search the relevant search information as a {@code Search} object
     * @return the metadata as a {@code String}
     */
    public static String makeMetadata(Search search) {
        return "#Description: '" +
                search.getDescription() +
                "';Data set: 'Cellosaurus release " +
                search.getCellosaurusRelease() +
                "';Run on: '" +
                search.getRunOn() +
                "';Tool version: '" +
                search.getToolVersion() +
                "';Species: '" +
                search.getParameters().getSpecies() +
                "';Algorithm: '" +
                search.getParameters().getAlgorithm() +
                "';Scoring mode: '" +
                search.getParameters().getScoringMode() +
                "';Score filter: '" +
                search.getParameters().getScoreFilter() +
                "';Min markers: '" +
                search.getParameters().getMinMarkers() +
                "';Max results: '" +
                search.getParameters().getMaxResults() +
                "';Include Amelogenin: '" +
                search.getParameters().isIncludeAmelogenin() +
                "'";
    }

    /**
     * Extract and validate the output format.
     *
     * @param map a {@code MultivaluedMap} representing the parameter keys and values
     * @return the format value as a {@code String}
     */
    public static String getOutputFormat(MultivaluedMap<String, String> map) {
        String format = "";

        for (String key: map.keySet()) {
            if (key.equalsIgnoreCase("outputformat")) {
                if (map.getFirst(key).equalsIgnoreCase("json")) {
                    format = "JSON";
                } else if (map.getFirst(key).equalsIgnoreCase("csv")) {
                    format = "CSV";
                } else if (map.getFirst(key).equalsIgnoreCase("xlsx")) {
                    format = "XLSX";
                } else {
                    throw new IllegalArgumentException(map.getFirst(key));
                }
                break;
            }
        }
        return format;
    }

    /**
     * Extract and validate the output format.
     *
     * @param pair an entry pair composed of a {@code String} key and a {@code JsonElement}
     * @return the format value as a {@code String}
     */
    public static String getOutputFormat(Map.Entry<String, JsonElement> pair) {
        String format = "";

        if (pair.getKey().equalsIgnoreCase("outputformat")) {
            if (pair.getValue().getAsString().equalsIgnoreCase("json")) {
                format = "JSON";
            } else if (pair.getValue().getAsString().equalsIgnoreCase("csv")) {
                format = "CSV";
            } else if (pair.getValue().getAsString().equalsIgnoreCase("xlsx")) {
                format = "XLSX";
            } else {
                throw new IllegalArgumentException(pair.getValue().getAsString());
            }
        }
        return format;
    }
}

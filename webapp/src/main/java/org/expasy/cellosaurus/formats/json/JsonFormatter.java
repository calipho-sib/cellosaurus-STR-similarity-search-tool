package org.expasy.cellosaurus.formats.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.expasy.cellosaurus.genomics.str.Allele;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.wrappers.Parameters;
import org.expasy.cellosaurus.wrappers.Search;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class handling the conversion of JSON encoded search results to various formats.
 */
public class JsonFormatter {

    /**
     * Convert the search results and metadata from the JSON format back to a {@code Search} object
     *
     * @param json the search results and metadata as a JSON {@code String}
     * @return the search results and metadata as a {@code Search} object
     */
    public Search toSearch(String json) {
        JsonObject searchObject = new JsonParser().parse(json).getAsJsonObject();

        JsonObject parametersObject = searchObject.getAsJsonObject("parameters");
        List<Marker> markers = formatMarkers(parametersObject);

        String algorithm = parametersObject.get("algorithm").getAsString();
        String scoringMode = parametersObject.get("scoringMode").getAsString();
        int scoreFilter = parametersObject.get("scoreFilter").getAsInt();
        int minMarkers = parametersObject.get("minMarkers").getAsInt();
        int maxResults = parametersObject.get("maxResults").getAsInt();
        boolean includeAmelogenin = parametersObject.get("includeAmelogenin").getAsBoolean();

        Parameters parameters = new Parameters(algorithm, scoringMode, scoreFilter, minMarkers, maxResults, includeAmelogenin);
        parameters.setMarkers(markers);

        List<CellLine> cellLines = new ArrayList<>();
        for (JsonElement cellLineElement : searchObject.getAsJsonArray("results")) {
            JsonObject cellLineObject = cellLineElement.getAsJsonObject();

            List<Profile> profiles = new ArrayList<>();
            for (JsonElement profilesElement : cellLineObject.getAsJsonArray("profiles")) {
                JsonObject profilesObject = profilesElement.getAsJsonObject();

                Profile profile = new Profile(formatMarkers(profilesObject));
                profile.setScore(profilesObject.get("score").getAsDouble());
                profile.setMarkerNumber(profilesObject.get("markerNumber").getAsInt());
                profile.setAlleleNumber(profilesObject.get("alleleNumber").getAsInt());

                profiles.add(profile);
            }
            String accession = cellLineObject.get("accession").getAsString();
            String name = cellLineObject.get("name").getAsString();
            String species = cellLineObject.get("species").getAsString();

            CellLine cellLine = new CellLine(accession, name, species);
            cellLine.setBestScore(cellLineObject.get("bestScore").getAsDouble());
            cellLine.setProblematic(cellLineObject.get("problematic").getAsBoolean());
            if (cellLineObject.get("problem") != null) {
                cellLine.setProblem(cellLineObject.get("problem").getAsString());
            }
            cellLine.getProfiles().addAll(profiles);
            cellLines.add(cellLine);
        }

        String description = searchObject.get("description").getAsString();
        String cellosaurusRelease = searchObject.get("cellosaurusRelease").getAsString();
        String runOn = searchObject.get("runOn").getAsString();
        String toolVersion = searchObject.get("toolVersion").getAsString();
        Search search = new Search(description, cellosaurusRelease, runOn, toolVersion);
        search.setParameters(parameters);
        search.setResults(cellLines);

        return search;
    }

    /**
     * Extract the STR markers from a {@code JsonObject} and return it as a list of {@code Marker} objects
     *
     * @param object the {@code JsonObject} containg the  STR marker data
     * @return the list of STR markers contained in the {@code JsonObject}
     */
    private List<Marker> formatMarkers(JsonObject object) {
        List<Marker> markers = new ArrayList<>();

        for (JsonElement markersElement : object.getAsJsonArray("markers")) {
            JsonObject markersObject = markersElement.getAsJsonObject();

            List<Allele> alleles = new ArrayList<>();
            for (JsonElement allelesElement : markersObject.getAsJsonArray("alleles")) {
                JsonObject alleleObject = allelesElement.getAsJsonObject();

                Allele allele = new Allele(alleleObject.get("value").getAsString());
                if (alleleObject.get("matched") != null) {
                    allele.setMatched(alleleObject.get("matched").getAsBoolean());
                }
                alleles.add(allele);
            }
            Set<String> sources = new LinkedHashSet<>();
            if (markersObject.getAsJsonArray("sources") != null) {
                for (JsonElement sourcesElement : markersObject.getAsJsonArray("sources")) {
                    sources.add(sourcesElement.getAsString());
                }
            }
            Marker marker = new Marker(markersObject.get("name").getAsString(), alleles);
            if (markersObject.get("conflicted") != null) {
                marker.setConflicted(markersObject.get("conflicted").getAsBoolean());
            }
            if (markersObject.get("searched") != null) {
                marker.setSearched(markersObject.get("searched").getAsBoolean());
            }
            marker.setSources(sources);
            markers.add(marker);
        }
        return markers;
    }
}

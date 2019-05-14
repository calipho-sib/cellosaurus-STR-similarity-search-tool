package org.expasy.cellosaurus.formats.json;

import com.google.gson.Gson;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.formats.xml.XmlParser;
import org.expasy.cellosaurus.wrappers.Search;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonFormatterTest {

    public JsonFormatterTest() throws IOException {
        XmlParser xmlParser = new XmlParser(ClassLoader.getSystemResource("cellosaurus.min.xml").getPath());
        Manager.cellLines = xmlParser.getCellLines();
        Manager.database = xmlParser.getDatabase();
    }

    @Test
    public void toSearchTest1() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("Am", "X");
        parameters.add("CSF1P0", "12,13");
        parameters.add("D5S818", "10,13");
        parameters.add("D7S820", "8,10");
        parameters.add("D13S317", "12");
        parameters.add("D16S539", "8,13");
        parameters.add("TH01", "7,9");
        parameters.add("TPOX", "11");
        parameters.add("vWA", "14,15");
        parameters.add("algorithm", "2");
        parameters.add("scoringMode", "3");
        parameters.add("maxResults", "44");
        parameters.add("scoreFilter", "80");
        parameters.add("description", "toSearchTest1");

        Gson gson = new Gson();
        Search expected = Manager.search(parameters);
        String json = gson.toJson(expected);

        JsonFormatter jsonFormatter = new JsonFormatter();
        assertEquals(expected, jsonFormatter.toSearch(json));
    }

    @Test
    public void toSearchTest2() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("Amel", "X");
        parameters.add("CSF1P0", "12");
        parameters.add("D5S818", "10,11");
        parameters.add("D7S820", "9");
        parameters.add("D13S317", "12");
        parameters.add("D16S539", "8,13");
        parameters.add("TH01", "7,9");
        parameters.add("TPOX", "11,13");
        parameters.add("vWA", "14,15");
        parameters.add("algorithm", "2");
        parameters.add("scoringMode", "3");
        parameters.add("maxResults", "44");
        parameters.add("scoreFilter", "80");
        parameters.add("description", "toSearchTest2");

        Gson gson = new Gson();
        Search expected = Manager.search(parameters);
        String json = gson.toJson(expected);

        JsonFormatter jsonFormatter = new JsonFormatter();
        assertEquals(expected, jsonFormatter.toSearch(json));
    }


    @Test
    public void toSearchTest3() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("Amelogenin", "X");
        parameters.add("CSF1PO", "12,13");
        parameters.add("D5S818", "10,13");
        parameters.add("D7S820", "8,9");
        parameters.add("D13S317", "10");
        parameters.add("D16S539", "13");
        parameters.add("TH01", "7,8");
        parameters.add("TPOX", "11,12");
        parameters.add("vWA", "14,16");
        parameters.add("algorithm", "2");
        parameters.add("scoringMode", "3");
        parameters.add("maxResults", "200");
        parameters.add("scoreFilter", "40");
        parameters.add("description", "toSearchTest3");

        Gson gson = new Gson();
        Search expected = Manager.search(parameters);
        String json = gson.toJson(expected);

        JsonFormatter jsonFormatter = new JsonFormatter();
        assertEquals(expected, jsonFormatter.toSearch(json));
    }
}
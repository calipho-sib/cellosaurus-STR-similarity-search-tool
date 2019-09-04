package org.expasy.cellosaurus.formats.csv;

import com.google.gson.Gson;
import org.expasy.cellosaurus.Manager;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.formats.json.JsonFormatter;
import org.expasy.cellosaurus.formats.xml.XmlParser;
import org.expasy.cellosaurus.genomics.str.Species;
import org.expasy.cellosaurus.wrappers.Search;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvFormatterTest {

    public CsvFormatterTest() throws IOException {
        Species.HUMAN.getCellLines().clear();
        Parser parser = new XmlParser();
        parser.parse(ClassLoader.getSystemResource("cellosaurus.min.xml").getPath());
    }
    
    @Test
    public void toCsvTest1() throws IOException {
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
        parameters.add("description", "toCsvTest1");

        Gson gson = new Gson();
        Search search = Manager.search(parameters);
        String json = gson.toJson(search);
        json = json.replaceAll("\\d{4}\\-\\w{3}\\-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\+0", "0000-Xxx-00 00:00:00 UTC+0");
        json = json.replace("\"toolVersion\":\"" + search.getToolVersion() + "\"", "\"toolVersion\":\"x.x.x\"");

        JsonFormatter jsonFormatter = new JsonFormatter();
        Search newSearch = jsonFormatter.toSearch(json);

        CsvFormatter csvFormatter = new CsvFormatter();

        File file = new File(ClassLoader.getSystemResource("csv/csv_test_1.csv").getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        assertEquals(sb.toString(), csvFormatter.toCsv(newSearch));
    }

    @Test
    public void toCsvTest2() throws IOException {
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
        parameters.add("maxResults", "40");
        parameters.add("scoreFilter", "20");
        parameters.add("description", "toCsvTest2");

        Gson gson = new Gson();
        Search search = Manager.search(parameters);
        String json = gson.toJson(search);
        json = json.replaceAll("\\d{4}\\-\\w{3}\\-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\+0", "0000-Xxx-00 00:00:00 UTC+0");
        json = json.replace("\"toolVersion\":\"" + search.getToolVersion() + "\"", "\"toolVersion\":\"x.x.x\"");

        JsonFormatter jsonFormatter = new JsonFormatter();
        Search newSearch = jsonFormatter.toSearch(json);

        CsvFormatter csvFormatter = new CsvFormatter();

        File file = new File(ClassLoader.getSystemResource("csv/csv_test_2.csv").getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        assertEquals(sb.toString(), csvFormatter.toCsv(newSearch));
    }
    
    @Test
    public void toCsvTest3() throws IOException {
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
        parameters.add("scoreFilter", "50");
        parameters.add("description", "toCsvTest3");

        Gson gson = new Gson();
        Search search = Manager.search(parameters);
        String json = gson.toJson(search);
        json = json.replaceAll("\\d{4}\\-\\w{3}\\-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\+0", "0000-Xxx-00 00:00:00 UTC+0");
        json = json.replace("\"toolVersion\":\"" + search.getToolVersion() + "\"", "\"toolVersion\":\"x.x.x\"");

        JsonFormatter jsonFormatter = new JsonFormatter();
        Search newSearch = jsonFormatter.toSearch(json);

        CsvFormatter csvFormatter = new CsvFormatter();

        File file = new File(ClassLoader.getSystemResource("csv/csv_test_3.csv").getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        assertEquals(sb.toString(), csvFormatter.toCsv(newSearch));
    }
}
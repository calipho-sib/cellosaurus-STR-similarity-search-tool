package org.expasy.cellosaurus;

import com.google.gson.Gson;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.formats.xml.XmlParser;
import org.expasy.cellosaurus.wrappers.Search;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    public ManagerTest() throws IOException {
        Parser parser = new XmlParser();
        parser.parse(ClassLoader.getSystemResource("cellosaurus.min.xml").getPath());
        Manager.cellLines = parser.getSpecies("Homo sapiens").getCellLines();
        Manager.database = parser.getDatabase();
    }

    @Test
    public void searchTest1() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("Am", "X");
        parameters.add("CSF1P0", "12,13");
        parameters.add("D5S818", "10,13");
        parameters.add("D7S820", "8,10");
        parameters.add("D13S317", "12");
        parameters.add("D16S539", "8,13");
        parameters.add("TH01", "7,9");
        parameters.add("TPOX", "11");
        parameters.add("vWA", "14,17");
        parameters.add("algorithm", "2");
        parameters.add("scoringMode", "1");
        parameters.add("maxResults", "25");
        parameters.add("scoreFilter", "70");
        parameters.add("description", "searchTest1");

        Gson gson = new Gson();
        Search search = Manager.search(parameters);

        String actual = gson.toJson(search);
        actual = actual.replaceAll("\\d{4}\\-\\w{3}\\-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\+0", "0000-Xxx-00 00:00:00 UTC+0");
        String expected = "{\"description\":\"searchTest1\",\"cellosaurusRelease\":\"29.0\",\"runOn\":\"0000-Xxx-00 00:00:00 UTC+0\",\"toolVersion\":\"1.1.0\",\"parameters\":{\"algorithm\":\"Masters (vs. query)\",\"scoringMode\":\"Non-empty makers\",\"scoreFilter\":70,\"maxResults\":25,\"includeAmelogenin\":false,\"markers\":[{\"name\":\"Amelogenin\",\"alleles\":[{\"value\":\"X\"}]},{\"name\":\"CSF1PO\",\"alleles\":[{\"value\":\"12\"},{\"value\":\"13\"}]},{\"name\":\"D5S818\",\"alleles\":[{\"value\":\"10\"},{\"value\":\"13\"}]},{\"name\":\"D7S820\",\"alleles\":[{\"value\":\"8\"},{\"value\":\"10\"}]},{\"name\":\"D13S317\",\"alleles\":[{\"value\":\"12\"}]},{\"name\":\"D16S539\",\"alleles\":[{\"value\":\"8\"},{\"value\":\"13\"}]},{\"name\":\"TH01\",\"alleles\":[{\"value\":\"7\"},{\"value\":\"9\"}]},{\"name\":\"TPOX\",\"alleles\":[{\"value\":\"11\"}]},{\"name\":\"vWA\",\"alleles\":[{\"value\":\"14\"},{\"value\":\"17\"}]}]},\"results\":[{\"accession\":\"CVCL_2261\",\"name\":\"1-7HB2\",\"species\":\"Homo sapiens\",\"bestScore\":100.0,\"problematic\":false,\"profiles\":[{\"score\":100.0,\"markerNumber\":8,\"alleleNumber\":14,\"markers\":[{\"name\":\"Amelogenin\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"X\",\"matched\":true}]},{\"name\":\"CSF1PO\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"12\",\"matched\":true},{\"value\":\"13\",\"matched\":true}]},{\"name\":\"D5S818\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"10\",\"matched\":true},{\"value\":\"13\",\"matched\":true}]},{\"name\":\"D7S820\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"8\",\"matched\":true},{\"value\":\"10\",\"matched\":true}]},{\"name\":\"D13S317\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"12\",\"matched\":true}]},{\"name\":\"D16S539\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"8\",\"matched\":true},{\"value\":\"13\",\"matched\":true}]},{\"name\":\"TH01\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"7\",\"matched\":true},{\"value\":\"9\",\"matched\":true}]},{\"name\":\"TPOX\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"11\",\"matched\":true}]},{\"name\":\"vWA\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"14\",\"matched\":true},{\"value\":\"17\",\"matched\":true}]}]}]},{\"accession\":\"CVCL_4992\",\"name\":\"CH1 [Human ovarian carcinoma]\",\"species\":\"Homo sapiens\",\"bestScore\":75.0,\"problematic\":true,\"problem\":\"Contaminated. Shown to be a PA-1 derivative (PubMed\\u003d22710073).\",\"profiles\":[{\"score\":75.0,\"markerNumber\":2,\"alleleNumber\":4,\"markers\":[{\"name\":\"Amelogenin\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"X\",\"matched\":true}]},{\"name\":\"D8S1179\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"14\",\"matched\":false},{\"value\":\"15\",\"matched\":false}]},{\"name\":\"D21S11\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"29\",\"matched\":false},{\"value\":\"31.2\",\"matched\":false}]},{\"name\":\"FGA\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"24\",\"matched\":false}]},{\"name\":\"TH01\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"7\",\"matched\":true},{\"value\":\"9\",\"matched\":true}]},{\"name\":\"vWA\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"15\",\"matched\":false},{\"value\":\"17\",\"matched\":true}]}]}]},{\"accession\":\"CVCL_X012\",\"name\":\"CH1-cisR\",\"species\":\"Homo sapiens\",\"bestScore\":75.0,\"problematic\":true,\"problem\":\"Contaminated. Parent cell line (CH1) has been shown to be a PA-1 derivative.\",\"profiles\":[{\"score\":75.0,\"markerNumber\":2,\"alleleNumber\":4,\"markers\":[{\"name\":\"Amelogenin\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"X\",\"matched\":true}]},{\"name\":\"D8S1179\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"14\",\"matched\":false},{\"value\":\"15\",\"matched\":false}]},{\"name\":\"D18S51\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"15\",\"matched\":false},{\"value\":\"18\",\"matched\":false}]},{\"name\":\"D21S11\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"29\",\"matched\":false},{\"value\":\"31.2\",\"matched\":false}]},{\"name\":\"FGA\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"24\",\"matched\":false}]},{\"name\":\"TH01\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"7\",\"matched\":true},{\"value\":\"9\",\"matched\":true}]},{\"name\":\"vWA\",\"conflicted\":false,\"sources\":[],\"alleles\":[{\"value\":\"15\",\"matched\":false},{\"value\":\"17\",\"matched\":true}]}]}]}]}";
        assertEquals(expected, actual);
    }

    @Test
    public void searchTest2() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("Amel", "X");
        parameters.add("CSF1PO", "13,14");
        parameters.add("D5S818", "10");
        parameters.add("D7S820", "8");
        parameters.add("D13S317", "11");
        parameters.add("D16S539", "8,9");
        parameters.add("THO1", "7,8");
        parameters.add("TPOX", "12");
        parameters.add("vWA", "10,11");
        parameters.add("algorithm", "3");
        parameters.add("scoringMode", "3");
        parameters.add("maxResults", "1");
        parameters.add("scoreFilter", "25");
        parameters.add("description", "searchTest2");

        Gson gson = new Gson();
        Search search = Manager.search(parameters);

        String actual = gson.toJson(search);
        actual = actual.replaceAll("\\d{4}\\-\\w{3}\\-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\+0", "0000-Xxx-00 00:00:00 UTC+0");
        String expected = "{\"description\":\"searchTest2\",\"cellosaurusRelease\":\"29.0\",\"runOn\":\"0000-Xxx-00 00:00:00 UTC+0\",\"toolVersion\":\"1.1.0\",\"parameters\":{\"algorithm\":\"Masters (vs. reference)\",\"scoringMode\":\"Reference markers\",\"scoreFilter\":25,\"maxResults\":1,\"includeAmelogenin\":false,\"markers\":[{\"name\":\"Amelogenin\",\"alleles\":[{\"value\":\"X\"}]},{\"name\":\"CSF1PO\",\"alleles\":[{\"value\":\"13\"},{\"value\":\"14\"}]},{\"name\":\"D5S818\",\"alleles\":[{\"value\":\"10\"}]},{\"name\":\"D7S820\",\"alleles\":[{\"value\":\"8\"}]},{\"name\":\"D13S317\",\"alleles\":[{\"value\":\"11\"}]},{\"name\":\"D16S539\",\"alleles\":[{\"value\":\"8\"},{\"value\":\"9\"}]},{\"name\":\"TH01\",\"alleles\":[{\"value\":\"7\"},{\"value\":\"8\"}]},{\"name\":\"TPOX\",\"alleles\":[{\"value\":\"12\"}]},{\"name\":\"vWA\",\"alleles\":[{\"value\":\"10\"},{\"value\":\"11\"}]}]},\"results\":[{\"accession\":\"CVCL_8175\",\"name\":\"C125-PM\",\"species\":\"Homo sapiens\",\"bestScore\":38.46153846153847,\"problematic\":false,\"stability\":\"Stable (MSS) (PubMed\\u003d25926053).\",\"profiles\":[{\"score\":38.46153846153847,\"markerNumber\":9,\"alleleNumber\":13,\"markers\":[{\"name\":\"Amelogenin\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"X\",\"matched\":true}]},{\"name\":\"CSF1PO\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"11\",\"matched\":false},{\"value\":\"14\",\"matched\":true}]},{\"name\":\"D5S818\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"9\",\"matched\":false}]},{\"name\":\"D7S820\",\"conflicted\":true,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"8\",\"matched\":true},{\"value\":\"10\",\"matched\":false}]},{\"name\":\"D13S317\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"11\",\"matched\":true},{\"value\":\"12\",\"matched\":false}]},{\"name\":\"D16S539\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"9\",\"matched\":true}]},{\"name\":\"D21S11\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"28\",\"matched\":false}]},{\"name\":\"TH01\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"8\",\"matched\":true}]},{\"name\":\"TPOX\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"8\",\"matched\":false}]},{\"name\":\"vWA\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"17\",\"matched\":false},{\"value\":\"19\",\"matched\":false}]}]},{\"score\":30.76923076923077,\"markerNumber\":9,\"alleleNumber\":13,\"markers\":[{\"name\":\"Amelogenin\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"X\",\"matched\":true}]},{\"name\":\"CSF1PO\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"11\",\"matched\":false},{\"value\":\"14\",\"matched\":true}]},{\"name\":\"D5S818\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"9\",\"matched\":false}]},{\"name\":\"D7S820\",\"conflicted\":true,\"sources\":[\"ECACC\"],\"alleles\":[{\"value\":\"8.2\",\"matched\":false},{\"value\":\"10\",\"matched\":false}]},{\"name\":\"D13S317\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"11\",\"matched\":true},{\"value\":\"12\",\"matched\":false}]},{\"name\":\"D16S539\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"9\",\"matched\":true}]},{\"name\":\"D21S11\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"28\",\"matched\":false}]},{\"name\":\"TH01\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"8\",\"matched\":true}]},{\"name\":\"TPOX\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"8\",\"matched\":false}]},{\"name\":\"vWA\",\"conflicted\":false,\"sources\":[\"PubMed\\u003d25926053\"],\"alleles\":[{\"value\":\"17\",\"matched\":false},{\"value\":\"19\",\"matched\":false}]}]}]}]}";
        assertEquals(expected, actual);
    }

    @Test
    public void searchTest3() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("Amelogenin", "X,Y");
        parameters.add("CSF1PO", "15");
        parameters.add("D5S818", "9");
        parameters.add("D7S820", "8,9");
        parameters.add("D13S317", "11,13");
        parameters.add("D16S539", "7,9");
        parameters.add("TH01", "8");
        parameters.add("TPOX", "15");
        parameters.add("VWA", "10");
        parameters.add("algorithm", "1");
        parameters.add("scoringMode", "1");
        parameters.add("maxResults", "50");
        parameters.add("scoreFilter", "100");
        parameters.add("description", "searchTest3");

        Gson gson = new Gson();
        Search search = Manager.search(parameters);

        String actual = gson.toJson(search);
        actual = actual.replaceAll("\\d{4}\\-\\w{3}\\-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\+0", "0000-Xxx-00 00:00:00 UTC+0");
        String expected = "{\"description\":\"searchTest3\",\"cellosaurusRelease\":\"29.0\",\"runOn\":\"0000-Xxx-00 00:00:00 UTC+0\",\"toolVersion\":\"1.1.0\",\"parameters\":{\"algorithm\":\"Tanabe\",\"scoringMode\":\"Non-empty makers\",\"scoreFilter\":100,\"maxResults\":50,\"includeAmelogenin\":false,\"markers\":[{\"name\":\"Amelogenin\",\"alleles\":[{\"value\":\"X\"},{\"value\":\"Y\"}]},{\"name\":\"CSF1PO\",\"alleles\":[{\"value\":\"15\"}]},{\"name\":\"D5S818\",\"alleles\":[{\"value\":\"9\"}]},{\"name\":\"D7S820\",\"alleles\":[{\"value\":\"8\"},{\"value\":\"9\"}]},{\"name\":\"D13S317\",\"alleles\":[{\"value\":\"11\"},{\"value\":\"13\"}]},{\"name\":\"D16S539\",\"alleles\":[{\"value\":\"7\"},{\"value\":\"9\"}]},{\"name\":\"TH01\",\"alleles\":[{\"value\":\"8\"}]},{\"name\":\"TPOX\",\"alleles\":[{\"value\":\"15\"}]},{\"name\":\"vWA\",\"alleles\":[{\"value\":\"10\"}]}]},\"results\":[]}";
        assertEquals(expected, actual);
    }

    @Test
    public void searchTest4() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("algorithm", "4");

        assertThrows(IllegalArgumentException.class, ()-> Manager.search(parameters));
    }

    @Test
    public void searchTest5() {
        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
        parameters.add("scoringMode", "0");

        assertThrows(IllegalArgumentException.class, ()-> Manager.search(parameters));
    }
}
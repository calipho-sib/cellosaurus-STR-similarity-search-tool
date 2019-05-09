package org.expasy.cellosaurus.genomics.str;

import java.util.Arrays;
import java.util.List;

/**
 * Class providing the list of core and minor STR markers, representing thw two columns in the web user interface, as
 * constants.
 */
public final class HumanMarkers {
    public final static List<Marker> CORE_MARKERS = Arrays.asList(
            new Marker("Amelogenin"),
            new Marker("CSF1PO"),
            new Marker("D2S1338"),
            new Marker("D3S1358"),
            new Marker("D5S818"),
            new Marker("D7S820"),
            new Marker("D8S1179"),
            new Marker("D13S317"),
            new Marker("D16S539"),
            new Marker("D18S51"),
            new Marker("D19S433"),
            new Marker("D21S11"),
            new Marker("FGA"),
            new Marker("Penta_D"),
            new Marker("Penta_E"),
            new Marker("TH01"),
            new Marker("TPOX"),
            new Marker("vWA")
    );
    public final static List<Marker> MINOR_MARKERS = Arrays.asList(
            new Marker("D10S1248"),
            new Marker("D1S1656"),
            new Marker("D2S441"),
            new Marker("D6S1043"),
            new Marker("D12S391"),
            new Marker("D22S1045"),
            new Marker("DXS101"),
            new Marker("DYS391"),
            new Marker("F13A01"),
            new Marker("F13B"),
            new Marker("FESFPS"),
            new Marker("LPL"),
            new Marker("Penta_C"),
            new Marker("SE33")
    );
}

package org.expasy.cellosaurus.format.xml;

import org.expasy.cellosaurus.bio.CellLine;
import org.expasy.cellosaurus.bio.str.ConflictResolver;
import org.expasy.cellosaurus.bio.str.Marker;
import org.expasy.cellosaurus.db.Database;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Database database;

    private List<CellLine> cellLines = new ArrayList<>();

    public Parser(String infile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                boolean bAccession = false;
                boolean bName = false;
                boolean bStrList = false;
                boolean bMarkerList = false;
                boolean bAlleles = false;
                boolean bSource = false;
                boolean bSpecies = false;

                List<String> sources = new ArrayList<>();
                List<String> references = new ArrayList<>();
                List<Marker> markers = new ArrayList<>();

                CellLine cellLine;
                ConflictResolver conflictResolver;

                Marker marker;
                String markerName;
                boolean markerConflicted;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    switch (qName) {
                        case "release":
                            String version = attributes.getValue("version");
                            String updated = attributes.getValue("updated");
                            int cellLines = Integer.valueOf(attributes.getValue("nb-cell-lines"));
                            int publications = Integer.valueOf(attributes.getValue("nb-publications"));
                            database = new Database(version, updated, cellLines, publications);
                            break;
                        case "cell-line":
                            cellLine = new CellLine();
                            conflictResolver = new ConflictResolver();
                            break;
                        case "accession":
                            bAccession = true;
                            break;
                        case "name":
                            if (attributes.getValue("type").equals("identifier")) {
                                bName = true;
                            }
                            break;
                        case "comment":
                            if (attributes.getValue("category").equals("Problematic cell line")) {
                                cellLine.setProblematic(true);
                            }
                            break;
                        case "str-list":
                            bStrList = true;
                            break;
                        case "marker-list":
                            bMarkerList = true;
                            break;
                        case "marker":
                            markerName = attributes.getValue("id");
                            markerConflicted = attributes.getValue("conflict").equals("true");
                            break;
                        case "marker-data":
                            marker = new Marker(markerName, markerConflicted);
                            break;
                        case "alleles":
                            bAlleles = true;
                            break;
                        case "source-list":
                            sources.clear();
                            references.clear();
                            break;
                        case "source":
                            bSource = true;
                            break;
                        case "reference":
                            if (bMarkerList) {
                                String reference = attributes.getValue("resource-internal-ref");
                                if (reference.startsWith("PubMed")) {
                                    references.add(reference.split("_")[0]);
                                } else if (reference.startsWith("ubMed")) {
                                    //FIXME: REMOVE WHEN FIX IS LIVE
                                    references.add("P" + reference);
                                } else {
                                    references.add(reference);
                                }
                            }
                            break;
                        case "cv-term":
                            if (attributes.getValue("terminology").equals("NCBI-Taxonomy")) {
                                bSpecies = true;
                            }
                            break;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    switch (qName) {
                        case "cell-line":
                            if (conflictResolver.size() > 0) {
                                conflictResolver.resolve();
                                cellLine.setHaplotypes(conflictResolver.getHaplotypes());
                                cellLines.add(cellLine);
                            }
                            break;
                        case "str-list":
                            bStrList = false;
                            break;
                        case "marker":
                            conflictResolver.addMarkers(markers);
                            markers = new ArrayList<>();
                            break;
                        case "marker-list":
                            bMarkerList = false;
                            break;
                        case "marker-data":
                            sources.sort(String.CASE_INSENSITIVE_ORDER);
                            references.sort(String.CASE_INSENSITIVE_ORDER);
                            marker.addSources(sources);
                            marker.addSources(references);
                            markers.add(marker);
                            break;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    if (bAccession) {
                        cellLine.setAccession(new String(ch, start, length));
                        bAccession = false;
                    } else if (bName) {
                        cellLine.setName(new String(ch, start, length));
                        bName = false;
                    } else if (bAlleles) {
                        if (bStrList) {
                            for (String allele : new String(ch, start, length).split(",")) {
                                if (!allele.equals("Not_detected")) {
                                    marker.addAllele(allele);
                                }
                            }
                        }
                        bAlleles = false;
                    } else if (bSource) {
                        if (bMarkerList) {
                            sources.add(new String(ch, start, length));
                        }
                        bSource = false;
                    } else if (bSpecies) {
                        cellLine.setSpecies(new String(ch, start, length));
                        bSpecies = false;
                    }
                }
            };
            parser.parse(infile, handler);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Parser(File infile) {
        this(infile.toString());
    }

    public Database getDatabase() {
        return database;
    }

    public List<CellLine> getCellLines() {
        return cellLines;
    }
}

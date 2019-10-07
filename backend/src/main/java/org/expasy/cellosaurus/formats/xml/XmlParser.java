package org.expasy.cellosaurus.formats.xml;

import org.expasy.cellosaurus.db.Database;
import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.genomics.str.Allele;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Species;
import org.expasy.cellosaurus.genomics.str.utils.ConflictResolver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Parser for the XML version of the Cellosaurus database.
 */
public class XmlParser implements Parser {
    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(InputStream inputStream) throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                boolean bAccession = false;
                boolean bName = false;
                boolean bStrList = false;
                boolean bMarkerList = false;
                boolean bAlleles = false;
                boolean bSameOriginAs = false;
                boolean bDerivedFrom = false;
                boolean bSource = false;
                boolean bSpeciesList = false;
                boolean bSpecies = false;
                boolean bProblematic = false;

                List<String> accessions = new ArrayList<>();
                List<String> speciesNames = new ArrayList<>();
                List<Marker> markers = new ArrayList<>();
                List<String> sources = new ArrayList<>();
                List<String> references = new ArrayList<>();
                Set<String> origins = new HashSet<>();

                ConflictResolver conflictResolver = new ConflictResolver();

                String name;
                Marker marker;
                String markerName;
                boolean markerConflicted;

                String parent = "";
                String problem = "";

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    switch (qName) {
                        case "release":
                            Database.CELLOSAURUS.setVersion(attributes.getValue("version"));
                            Database.CELLOSAURUS.setUpdated(attributes.getValue("updated"));
                            Database.CELLOSAURUS.setCellLineCount(Integer.parseInt(attributes.getValue("nb-cell-lines")));
                            Database.CELLOSAURUS.setPublicationCount(Integer.parseInt(attributes.getValue("nb-publications")));
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
                                bProblematic = true;
                            }
                            break;
                        case "cv-term":
                            if (bSameOriginAs) {
                                if (attributes.getValue("terminology").equals("Cellosaurus")) {
                                    origins.add(attributes.getValue("accession"));
                                }
                            } else if (bDerivedFrom) {
                                if (attributes.getValue("terminology").equals("Cellosaurus")) {
                                    parent = attributes.getValue("accession");
                                }
                            } else if (bSpeciesList) {
                                if (attributes.getValue("terminology").equals("NCBI-Taxonomy")) {
                                    bSpecies = true;
                                }
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
                            if (markerName.startsWith("Mouse")) {
                                markerName = markerName.substring(6);
                            } else if (markerName.startsWith("Dog")) {
                                markerName = markerName.substring(4);
                            }
                            markerConflicted = attributes.getValue("conflict").equals("true");
                            break;
                        case "marker-data":
                            marker = new Marker(markerName);
                            marker.setConflicted(markerConflicted);
                            break;
                        case "alleles":
                            bAlleles = true;
                            break;
                        case "same-origin-as":
                            bSameOriginAs = true;
                            break;
                        case "derived-from":
                            bDerivedFrom = true;
                            break;
                        case "species-list":
                            bSpeciesList = true;
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
                                references.add(attributes.getValue("resource-internal-ref"));
                            }
                            break;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) {
                    switch (qName) {
                        case "cell-line":
                            String accession = accessions.get(0);

                            Collections.sort(speciesNames);
                            String speciesName = String.join("/", speciesNames);

                            Species species = Species.get(speciesName);
                            // Manual fix for CVCL_1875 as it possesses human STR markers only
                            if (accession.equals("CVCL_1875")) species = Species.HUMAN;
                            if (species != null)  {
                                species.addOrigins(origins);
                                species.addHierarchy(parent, accession);

                                if (!conflictResolver.isEmpty()) {
                                    CellLine cellLine = new CellLine(accession, name, speciesName);
                                    cellLine.setProblematic(!problem.isEmpty());
                                    if (!problem.isEmpty()) cellLine.setProblem(problem);
                                    cellLine.addProfiles(conflictResolver.resolve());
                                    species.addCellLine(cellLine);
                                }
                            }
                            conflictResolver = new ConflictResolver();
                            name = parent = problem = "";
                            accessions = new ArrayList<>();
                            speciesNames = new ArrayList<>();
                            origins = new HashSet<>();
                            break;
                        case "str-list":
                            bStrList = false;
                            break;
                        case "marker-list":
                            bMarkerList = false;
                            break;
                        case "marker":
                            conflictResolver.addMarkers(markers);
                            markers = new ArrayList<>();
                            break;
                        case "marker-data":
                            if (!marker.getName().equals("STR 9-2")) {
                                sources.sort(String.CASE_INSENSITIVE_ORDER);
                                references.sort(String.CASE_INSENSITIVE_ORDER);
                                marker.addSources(sources);
                                marker.addSources(references);
                                markers.add(marker);
                            }
                            break;
                        case "species-list":
                            bSpeciesList = false;
                            break;
                        case "same-origin-as":
                            bSameOriginAs = false;
                            if (!origins.isEmpty()) {
                                origins.add(accessions.get(0));
                            }
                            break;
                        case "derived-from":
                            bDerivedFrom = false;
                            break;
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) {
                    if (bAccession) {
                        accessions.add(new String(ch, start, length));
                        bAccession = false;
                    } else if (bName) {
                        name = new String(ch, start, length);
                        bName = false;
                    } else if (bAlleles) {
                        if (bStrList) {
                            for (String allele : new String(ch, start, length).split(",")) {
                                if (allele.equals("Not_detected")) {
                                    marker.addAllele(new Allele("ND"));
                                } else {
                                    marker.addAllele(new Allele(allele));
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
                        speciesNames.add(new String(ch, start, length));
                        bSpecies = false;
                    } else {
                        String trim = new String(ch, start, length).trim();
                        if (bProblematic) {
                            problem = trim;
                            bProblematic = false;
                        }
                    }
                }
            };
            parser.parse(inputStream, handler);
            inputStream.close();
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}

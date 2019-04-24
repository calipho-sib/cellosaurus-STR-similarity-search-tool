package org.expasy.cellosaurus.formats.xlsx;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.expasy.cellosaurus.formats.FormatsUtils;
import org.expasy.cellosaurus.formats.Writer;
import org.expasy.cellosaurus.formats.json.JsonFormatter;
import org.expasy.cellosaurus.genomics.str.Allele;
import org.expasy.cellosaurus.genomics.str.CellLine;
import org.expasy.cellosaurus.genomics.str.Marker;
import org.expasy.cellosaurus.genomics.str.Profile;
import org.expasy.cellosaurus.wrappers.Search;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class XlsxWriter implements Writer {
    private File tmpdir;
    private File xlsx;
    private Search search;

    public XlsxWriter(Search search) {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir") + "/STR-SST_XLSX" + UUID.randomUUID().toString());
        this.tmpdir.mkdir();
        this.search = search;
    }

    public XlsxWriter(String json) {
        this(new JsonFormatter().toSearch(json));
    }

    @Override
    public void write() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        List<Marker> headerMarkers = new ArrayList<>();
        for (Marker marker : search.getParameters().getMarkers()) {
            if (!marker.getName().equalsIgnoreCase("amelogenin") && !marker.getName().equalsIgnoreCase("amel")) {
                headerMarkers.add(new Marker(marker.getName()));
            }
        }

        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.GREY_80_PERCENT.index);
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);

        Cell headerCell0 = header.createCell(0);
        Cell headerCell1 = header.createCell(1);
        Cell headerCell2 = header.createCell(2);
        Cell headerCell3 = header.createCell(3);

        headerCell0.setCellValue("Accession");
        headerCell1.setCellValue("Name");
        headerCell2.setCellValue("Nº Markers");
        headerCell3.setCellValue("Score");

        headerCell0.setCellStyle(headerStyle);
        headerCell1.setCellStyle(headerStyle);
        headerCell2.setCellStyle(headerStyle);
        headerCell3.setCellStyle(headerStyle);

        for (int i = 0; i < headerMarkers.size(); i++) {
            Cell cell = header.createCell(i+4);

            if (headerMarkers.get(i).getName().equals("Amelogenin")) {
                cell.setCellValue("Amel");
            } else {
                cell.setCellValue(headerMarkers.get(i).getName().replace("_", " "));
            }
            cell.setCellStyle(headerStyle);
        }
        Cell headerCellN = header.createCell(headerMarkers.size()+4);
        headerCellN.setCellValue(FormatsUtils.metadata(search));
        headerCellN.setCellStyle(headerStyle);

        Font queryFont = workbook.createFont();
        queryFont.setColor(IndexedColors.ROYAL_BLUE.index);
        queryFont.setBold(true);

        CellStyle queryStyle = workbook.createCellStyle();
        queryStyle.setFont(queryFont);

        Row query = sheet.createRow(1);

        Cell queryCell0 = query.createCell(0);
        Cell queryCell1 = query.createCell(1);
        Cell queryCell2 = query.createCell(2);
        Cell queryCell3 = query.createCell(3);

        queryCell0.setCellValue("NA");
        queryCell1.setCellValue("Query");
        queryCell2.setCellValue("NA");
        queryCell3.setCellValue("NA");

        queryCell0.setCellStyle(queryStyle);
        queryCell1.setCellStyle(queryStyle);
        queryCell2.setCellStyle(queryStyle);
        queryCell3.setCellStyle(queryStyle);

        for (int i = 0; i < headerMarkers.size(); i++) {
            int idx = search.getParameters().getMarkers().indexOf(headerMarkers.get(i));
            if (idx > -1) {
                StringBuilder sb = new StringBuilder();
                for (Allele allele : search.getParameters().getMarkers().get(idx).getAlleles()) {
                    sb.append(allele.toString());
                    sb.append(',');
                }
                sb.setLength(sb.length() - 1);

                Cell cell = query.createCell(i+4);
                cell.setCellValue(sb.toString());
                cell.setCellStyle(queryStyle);
            }
        }

        Font blueFont = workbook.createFont();
        blueFont.setColor(IndexedColors.ROYAL_BLUE.index);

        Font greenFont = workbook.createFont();
        greenFont.setColor(IndexedColors.GREEN.index);

        Font orangeFont = workbook.createFont();
        orangeFont.setColor(IndexedColors.ORANGE.index);

        Font redFont = workbook.createFont();
        redFont.setColor(IndexedColors.RED.index);

        Font italicFont = workbook.createFont();
        italicFont.setItalic(true);

        Font underlinedFont = workbook.createFont();
        underlinedFont.setUnderline(Font.U_SINGLE);

        Font redUnderlinedFont = workbook.createFont();
        redUnderlinedFont.setUnderline(Font.U_SINGLE);
        redUnderlinedFont.setColor(IndexedColors.RED.index);

        CellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFont(greenFont);

        CellStyle orangeStyle = workbook.createCellStyle();
        orangeStyle.setFont(orangeFont);

        CellStyle redStyle = workbook.createCellStyle();
        redStyle.setFont(redFont);

        int c = 0;
        for (int i = 0; i < search.getResults().size(); i++) {
            CellLine cellLine = search.getResults().get(i);
            boolean best = true;

            for (Profile profile : cellLine.getProfiles()) {
                Row row = sheet.createRow(c + 2);
                Cell cell = row.createCell(0);

                String accession = cellLine.getAccession();
                StringBuilder sb = new StringBuilder();
                sb.append(accession);
                sb.append(" ");
                if (cellLine.getProfiles().size() > 1) {
                    if (best) {
                        sb.append("Best");
                        best = false;
                    } else {
                        sb.append("Worst");
                    }
                }
                RichTextString textString = new XSSFRichTextString(sb.toString());

                if (cellLine.isProblematic()) {
                    int y = cellLine.getProblem().length()/55+1;
                    addComment(cell, cellLine.getProblem(), 4, y);
                    textString.applyFont(0, accession.length(), redFont);
                } else {
                    textString.applyFont(0, accession.length(), blueFont);
                }
                if (accession.length() < sb.length()) {
                    textString.applyFont(accession.length(), sb.length(), italicFont);
                }
                Hyperlink link = workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                link.setAddress("https://web.expasy.org/cellosaurus/" + accession);
                cell.setCellValue(textString);
                cell.setHyperlink(link);

                cell = row.createCell(1);
                cell.setCellValue(cellLine.getName().replace("_", " "));
                cell = row.createCell(2);
                cell.setCellValue(profile.getMarkerNumber());

                if ((search.getParameters().isIncludeAmelogenin() && profile.getMarkerNumber() < 9) ||
                        (!search.getParameters().isIncludeAmelogenin() && profile.getMarkerNumber() < 8)) {
                    cell.setCellStyle(redStyle);
                }

                cell = row.createCell(3);
                cell.setCellValue(String.format("%.2f", profile.getScore()) + "%");

                if (search.getParameters().getAlgorithm().equals("Tanabe")) {
                    if (profile.getScore() >= 90.0) {
                        cell.setCellStyle(greenStyle);
                    } else if (profile.getScore() < 80.0) {
                        cell.setCellStyle(redStyle);
                    } else {
                        cell.setCellStyle(orangeStyle);
                    }
                } else {
                    if (profile.getScore() >= 80.0) {
                        cell.setCellStyle(greenStyle);
                    } else if (profile.getScore() < 60.0) {
                        cell.setCellStyle(redStyle);
                    } else {
                        cell.setCellStyle(orangeStyle);
                    }
                }

                List<List<Integer>> positions = new ArrayList<>();
                for (int j = 0; j < headerMarkers.size(); j++) {
                    sb.setLength(0);
                    positions.clear();

                    int idx = profile.getMarkers().indexOf(headerMarkers.get(j));
                    if (idx > -1) {
                        Marker marker =  profile.getMarkers().get(idx);

                        for (Allele allele : marker.getAlleles()) {
                            if (!allele.getMatched()) {
                                int length = sb.length();
                                positions.add(Arrays.asList(length, length + allele.getValue().length()));
                            }
                            sb.append(allele.toString());
                            sb.append(',');
                        }
                        sb.setLength(sb.length() - 1);

                        cell = row.createCell(j+4);
                        textString = new XSSFRichTextString(sb.toString());

                        if (marker.getConflicted()) {
                            sb.setLength(0);
                            sb.append(marker.getSources().size() == 1 ? "Source:\r\n" : "Sources:\r\n");
                            for (String source : marker.getSources()) {
                                sb.append(source);
                                sb.append("\r\n");
                            }

                            String sources = sb.toString();
                            int y = sources.length() - sources.replace("\n", "").length();

                            textString.applyFont(0, sb.length(), underlinedFont);
                            addComment(cell, sources, 2, y);
                        }
                        for (List<Integer> position : positions) {
                            if (marker.getConflicted()) {
                                textString.applyFont(position.get(0), position.get(1), redUnderlinedFont);
                            } else {
                                textString.applyFont(position.get(0), position.get(1), redFont);
                            }
                        }
                        cell.setCellValue(textString);
                    }
                }
                c++;
            }
        }
        sheet.setColumnWidth(0, 256*17);
        sheet.setColumnWidth(1, 256*17);
        sheet.setColumnWidth(2, 256*12);
        sheet.setColumnWidth(3, 256*12);
        for (int i = 3; i < headerMarkers.size() + 4; i++) {
            sheet.setColumnWidth(i, 256*8);
        }

        this.xlsx = new File(this.tmpdir + "Cellosaurus_STR_Results.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(this.xlsx);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }

    @Override
    public void close() {
        this.xlsx.delete();
        this.tmpdir.delete();
    }

    private void addComment(Cell cell, String message, int x, int y) {
        if (cell.getCellComment() != null) return;

        Drawing drawing = cell.getSheet().createDrawingPatriarch();
        CreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + x);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + y);

        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(message);
        comment.setVisible(Boolean.FALSE);
        comment.setString(str);

        cell.setCellComment(comment);
    }

    public File getXlsx() {
        return xlsx;
    }
}

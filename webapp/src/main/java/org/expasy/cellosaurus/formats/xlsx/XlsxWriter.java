package org.expasy.cellosaurus.formats.xlsx;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
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
    private final File tmpdir;
    private final File xlsx;
    private final XSSFWorkbook workbook;
    private final XSSFFont defaultFont;
    private final XSSFFont blueFont;
    private final XSSFFont redFont;
    private final XSSFFont italicFont;
    private final XSSFFont underlinedFont;
    private final XSSFFont redUnderlinedFont;
    private final XSSFCellStyle defaultStyle;
    private final XSSFCellStyle headerStyle;
    private final XSSFCellStyle queryStyle;
    private final XSSFCellStyle greenStyle;
    private final XSSFCellStyle orangeStyle;
    private final XSSFCellStyle redStyle;
    
    private int sheets = 0;

    public XlsxWriter() {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir") + "/STR-SST_XLSX_" + UUID.randomUUID().toString());
        this.tmpdir.mkdir();
        this.xlsx = new File(this.tmpdir + "/Cellosaurus_STR_Results.xlsx");
        this.workbook = new XSSFWorkbook();

        this.defaultFont = this.workbook.createFont();
        this.defaultFont.setFontName("Calibri");
        this.defaultStyle = this.workbook.createCellStyle();
        this.defaultStyle.setFont(this.defaultFont);

        XSSFFont headerFont = this.workbook.createFont();
        headerFont.setColor(IndexedColors.GREY_80_PERCENT.index);
        headerFont.setBold(true);
        this.headerStyle = this.workbook.createCellStyle();
        this.headerStyle.setFont(headerFont);
        
        XSSFFont queryFont = this.workbook.createFont();
        queryFont.setColor(IndexedColors.ROYAL_BLUE.index);
        queryFont.setBold(true);
        this.queryStyle = this.workbook.createCellStyle();
        this.queryStyle.setFont(queryFont);

        this.blueFont = this.workbook.createFont();
        this.blueFont.setColor(IndexedColors.ROYAL_BLUE.index);

        this.redFont = this.workbook.createFont();
        this.redFont.setColor(IndexedColors.RED.index);

        this.italicFont = this.workbook.createFont();
        this.italicFont.setItalic(true);

        this.underlinedFont = this.workbook.createFont();
        this.underlinedFont.setUnderline(XSSFFont.U_SINGLE);

        this.redUnderlinedFont = this.workbook.createFont();
        this.redUnderlinedFont.setUnderline(XSSFFont.U_SINGLE);
        this.redUnderlinedFont.setColor(IndexedColors.RED.index);

        XSSFFont greenFont = this.workbook.createFont();
        greenFont.setColor(IndexedColors.GREEN.index);
        this.greenStyle = this.workbook.createCellStyle();
        this.greenStyle.setFont(greenFont);

        XSSFFont orangeFont = this.workbook.createFont();
        orangeFont.setColor(IndexedColors.ORANGE.index);
        this.orangeStyle = this.workbook.createCellStyle();
        this.orangeStyle.setFont(orangeFont);

        this.redStyle = this.workbook.createCellStyle();
        this.redStyle.setFont(this.redFont);
    }

    public void add(String json) {
        add(new JsonFormatter().toSearch(json));
    }

    public void add(Search search) {
        XSSFSheet sheet = this.workbook.createSheet();
        String description = search.getDescription().replaceAll("[^\\w_\\-]", "_");
        String name = description.isEmpty() ? "Sheet" + (this.sheets+1) : description;
        this.workbook.setSheetName(this.sheets, name);
        this.sheets++;

        List<Marker> headerMarkers = new ArrayList<>();
        for (Marker marker : search.getParameters().getMarkers()) {
            if (!marker.getName().equalsIgnoreCase("amelogenin") && !marker.getName().equalsIgnoreCase("amel")) {
                headerMarkers.add(new Marker(marker.getName()));
            }
        }
        XSSFRow header = sheet.createRow(0);

        XSSFCell headerCell0 = header.createCell(0);
        XSSFCell headerCell1 = header.createCell(1);
        XSSFCell headerCell2 = header.createCell(2);
        XSSFCell headerCell3 = header.createCell(3);

        headerCell0.setCellValue("Accession");
        headerCell1.setCellValue("Name");
        headerCell2.setCellValue("Nº Markers");
        headerCell3.setCellValue("Score");

        headerCell0.setCellStyle(this.headerStyle);
        headerCell1.setCellStyle(this.headerStyle);
        headerCell2.setCellStyle(this.headerStyle);
        headerCell3.setCellStyle(this.headerStyle);

        for (int i = 0; i < headerMarkers.size(); i++) {
            XSSFCell cell = header.createCell(i + 4);

            if (headerMarkers.get(i).getName().equals("Amelogenin")) {
                cell.setCellValue("Amel");
            } else {
                cell.setCellValue(headerMarkers.get(i).getName().replace("_", " "));
            }
            cell.setCellStyle(this.headerStyle);
        }
        XSSFCell headerCellN = header.createCell(headerMarkers.size() + 4);
        headerCellN.setCellValue(FormatsUtils.metadata(search));
        headerCellN.setCellStyle(this.headerStyle);
        
        XSSFRow query = sheet.createRow(1);

        XSSFCell queryCell0 = query.createCell(0);
        XSSFCell queryCell1 = query.createCell(1);
        XSSFCell queryCell2 = query.createCell(2);
        XSSFCell queryCell3 = query.createCell(3);

        queryCell0.setCellValue("NA");
        queryCell1.setCellValue("Query");
        queryCell2.setCellValue("NA");
        queryCell3.setCellValue("NA");

        queryCell0.setCellStyle(this.queryStyle);
        queryCell1.setCellStyle(this.queryStyle);
        queryCell2.setCellStyle(this.queryStyle);
        queryCell3.setCellStyle(this.queryStyle);

        for (int i = 0; i < headerMarkers.size(); i++) {
            int idx = search.getParameters().getMarkers().indexOf(headerMarkers.get(i));
            if (idx > -1) {
                StringBuilder sb = new StringBuilder();
                for (Allele allele : search.getParameters().getMarkers().get(idx).getAlleles()) {
                    sb.append(allele.toString());
                    sb.append(',');
                }
                sb.setLength(sb.length() - 1);

                XSSFCell cell = query.createCell(i + 4);
                cell.setCellValue(sb.toString());
                cell.setCellStyle(this.queryStyle);
            }
        }
        
        int c = 0;
        for (int i = 0; i < search.getResults().size(); i++) {
            CellLine cellLine = search.getResults().get(i);
            boolean best = true;

            for (Profile profile : cellLine.getProfiles()) {
                XSSFRow row = sheet.createRow(c + 2);
                XSSFCell cell = row.createCell(0);

                redStyle.setFillForegroundColor((short) 22);
                cell.setCellStyle(this.redStyle);
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
                XSSFRichTextString textString = new XSSFRichTextString(sb.toString());

                if (cellLine.isProblematic()) {
                    int y = cellLine.getProblem().length() / 55 + 1;
                    addComment(cell, cellLine.getProblem(), 4, y);
                    textString.applyFont(0, accession.length(), this.redFont);
                } else {
                    textString.applyFont(0, accession.length(), this.blueFont);
                }
                if (accession.length() < sb.length()) {
                    textString.applyFont(accession.length(), sb.length(), this.italicFont);
                }
                Hyperlink link = this.workbook.getCreationHelper().createHyperlink(HyperlinkType.URL);
                link.setAddress("https://web.expasy.org/cellosaurus/" + accession);
                cell.setCellValue(textString);
                cell.setHyperlink(link);

                cell = row.createCell(1);
                cell.setCellValue(cellLine.getName());
                cell.setCellStyle(this.defaultStyle);

                cell = row.createCell(2);
                cell.setCellValue(profile.getMarkerNumber());
                if ((search.getParameters().isIncludeAmelogenin() && profile.getMarkerNumber() < 9) ||
                        (!search.getParameters().isIncludeAmelogenin() && profile.getMarkerNumber() < 8)) {
                    cell.setCellStyle(this.redStyle);
                } else {
                    cell.setCellStyle(this.defaultStyle);
                }

                cell = row.createCell(3);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(String.format("%.2f", profile.getScore()) + "%");

                if (search.getParameters().getAlgorithm().equals("Tanabe")) {
                    if (profile.getScore() >= 90.0) {
                        cell.setCellStyle(this.greenStyle);
                    } else if (profile.getScore() < 80.0) {
                        cell.setCellStyle(this.redStyle);
                    } else {
                        cell.setCellStyle(this.orangeStyle);
                    }
                } else {
                    if (profile.getScore() >= 80.0) {
                        cell.setCellStyle(this.greenStyle);
                    } else if (profile.getScore() < 60.0) {
                        cell.setCellStyle(this.redStyle);
                    } else {
                        cell.setCellStyle(this.orangeStyle);
                    }
                }

                List<List<Integer>> positions = new ArrayList<>();
                for (int j = 0; j < headerMarkers.size(); j++) {
                    sb.setLength(0);
                    positions.clear();

                    int idx = profile.getMarkers().indexOf(headerMarkers.get(j));
                    if (idx > -1) {
                        Marker marker = profile.getMarkers().get(idx);

                        for (Allele allele : marker.getAlleles()) {
                            if (!allele.getMatched()) {
                                int length = sb.length();
                                positions.add(Arrays.asList(length, length + allele.getValue().length()));
                            }
                            sb.append(allele.toString());
                            sb.append(',');
                        }
                        sb.setLength(sb.length() - 1);

                        cell = row.createCell(j + 4);
                        cell.setCellType(CellType.STRING);
                        textString = new XSSFRichTextString(sb.toString());

                        if (!marker.getConflicted()) {
                            textString.applyFont(0, sb.length(), this.defaultFont);
                        } else {
                            textString.applyFont(0, sb.length(), this.underlinedFont);

                            sb.setLength(0);
                            sb.append(marker.getSources().size() == 1 ? "Source:\r\n" : "Sources:\r\n");
                            for (String source : marker.getSources()) {
                                sb.append(source);
                                sb.append("\r\n");
                            }

                            String sources = sb.toString();
                            int y = sources.length() - sources.replace("\n", "").length();

                            addComment(cell, sources, 2, y);
                        }
                        for (List<Integer> position : positions) {
                            if (marker.getConflicted()) {
                                textString.applyFont(position.get(0), position.get(1), this.redUnderlinedFont);
                            } else {
                                textString.applyFont(position.get(0), position.get(1), this.redFont);
                            }
                        }
                        cell.setCellValue(textString);
                    }
                }
                c++;
            }
        }
        sheet.createFreezePane(0, 2);
        sheet.setColumnWidth(0, 256 * 17);
        sheet.setColumnWidth(1, 256 * 17);
        sheet.setColumnWidth(2, 256 * 12);
        sheet.setColumnWidth(3, 256 * 12);
        for (int i = 3; i < headerMarkers.size() + 4; i++) {
            sheet.setColumnWidth(i, 256 * 8);
        }
    }

    @Override
    public void write() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(this.xlsx);
        this.workbook.write(fileOutputStream);
        fileOutputStream.close();
        this.workbook.close();
    }

    @Override
    public void close() {
        this.xlsx.delete();
        this.tmpdir.delete();
    }

    private void addComment(XSSFCell cell, String message, int x, int y) {
        if (cell.getCellComment() != null) return;

        XSSFDrawing drawing = cell.getSheet().createDrawingPatriarch();
        XSSFCreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();
        XSSFClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + x);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + y);

        XSSFComment comment = drawing.createCellComment(anchor);
        XSSFRichTextString str = factory.createRichTextString(message);
        comment.setVisible(Boolean.FALSE);
        comment.setString(str);

        cell.setCellComment(comment);
    }

    public File getXlsx() {
        return xlsx;
    }
}

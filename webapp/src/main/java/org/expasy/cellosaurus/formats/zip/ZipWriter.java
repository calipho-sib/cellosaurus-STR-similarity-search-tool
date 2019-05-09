package org.expasy.cellosaurus.formats.zip;

import org.expasy.cellosaurus.formats.Writer;
import org.expasy.cellosaurus.formats.csv.CsvFormatter;
import org.expasy.cellosaurus.wrappers.Search;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class handling the compression and writing of multiple files into a single archive in the ZIP format. It is only used
 * for files in the CSV format in the latest versions of the STR Similarity Search Tool.
 */
public class ZipWriter implements Writer {
    private File tmpdir;
    private File zip;

    private List<File> files = new ArrayList<>();

    /**
     * Empty Constructor
     */
    public ZipWriter() {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir") + "/STR-SST_ZIP_" + UUID.randomUUID().toString());
        this.tmpdir.mkdir();
        this.zip = new File(this.tmpdir, "Cellosaurus_STR_Results.zip");
    }

    /**
     * Add a CSV file to be inserted into the ZIP archive
     *
     * {@inheritDoc}
     */
    @Override
    public void add(Search search) throws IOException {
        CsvFormatter csvFormatter = new CsvFormatter();

        String name = search.getDescription().replaceAll("[^\\w_\\-()]", "_");
        File tmp = new File(this.tmpdir, name + ".csv");

        int i = 0;
        while (files.contains(tmp)) {
            i++;
            tmp = new File(this.tmpdir, name + '(' + i + ").csv");
        }
        FileWriter fw = new FileWriter(tmp, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(csvFormatter.toCsv(search));
        bw.close();

        this.files.add(tmp);
    }

    /**
     * Write the ZIP archive as a temporary file on the server.
     *
     * {@inheritDoc}
     */
    @Override
    public void write() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(this.zip);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        for (File file : this.files) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));

            byte[] bytes = Files.readAllBytes(file.toPath());
            zipOutputStream.write(bytes, 0, bytes.length);
            zipOutputStream.closeEntry();
        }
        zipOutputStream.close();
    }

    /**
     * Close the {@code ZipWriter} instance by deleting all the temporary CSV files, ZIP file and containing folder from
     * the server.
     */
    @Override
    public void close() {
        for (File file : this.files) {
            file.delete();
        }
        this.zip.delete();
        this.tmpdir.delete();
    }

    public File getZip() {
        return zip;
    }
}

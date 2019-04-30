package org.expasy.cellosaurus.formats.zip;

import org.expasy.cellosaurus.formats.Writer;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter implements Writer {
    private File tmpdir;
    private File zip;

    private List<File> files = new ArrayList<>();

    public ZipWriter() {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir") + "/STR-SST_ZIP_" + UUID.randomUUID().toString());
        this.tmpdir.mkdir();
        this.zip = new File(this.tmpdir, "Cellosaurus_STR_Results.zip");
    }

    public void add(String description, String content) throws IOException {
        String name = description.replaceAll("[^\\w_\\-]", "_");
        File tmp = new File(this.tmpdir, name + ".csv");

        int i = 0;
        while (files.contains(tmp)) {
            i++;
            tmp = new File(this.tmpdir, name + '(' + i + ").csv");
        }

        FileWriter fw = new FileWriter(tmp, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();

        this.files.add(tmp);
    }

    public void add(File file) {
        this.files.add(file);
    }

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

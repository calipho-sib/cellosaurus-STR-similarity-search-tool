package org.expasy.cellosaurus.formats.zip;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Writer {
    private File tmpdir;
    private File zip;

    private List<File> files = new ArrayList<>();

    public Writer() {
        this.tmpdir = new File(System.getProperty("java.io.tmpdir") + "/STR-SST_" + UUID.randomUUID().toString());
        this.tmpdir.mkdir();
        this.zip = new File(this.tmpdir, "Cellosaurus_STR_Results.zip");
    }

    public void add(String name, String content) throws IOException {
        File tmp = new File(this.tmpdir, name.replaceAll("[^\\w_]", "_") + ".csv");

        int i = 0;
        while (files.contains(tmp)) {
            i++;
            tmp = new File(this.tmpdir, name.replaceAll("[^\\w_]", "_") + '(' + i + ").csv");
        }

        FileWriter fw = new FileWriter(tmp, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.flush();
        bw.close();

        this.files.add(tmp);
    }

    public void write() throws IOException {
        FileOutputStream fos = new FileOutputStream(this.zip);
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (File file : this.files) {
            zos.putNextEntry(new ZipEntry(file.getName()));

            byte[] bytes = Files.readAllBytes(file.toPath());
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
        }
        zos.close();
    }

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

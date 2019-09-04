package org.expasy.cellosaurus.formats;

import org.expasy.cellosaurus.db.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Interface regulating the key behaviours of format parsers
 */
public interface Parser {

    /**
     * @param path a file path {@code String}
     * @throws IOException if an I/O exception occurs
     */
    default void parse(String path) throws IOException {
        parse(new FileInputStream(new File(path)));
    }

    /**
     * @param file a {@code File} instance
     * @throws IOException if an I/O exception occurs
     */
    default void parse(File file) throws IOException {
        parse(new FileInputStream(file));
    }

    /**
     * @param url an {@code URL} of the file location
     * @throws IOException if an I/O exception occurs
     */
    default void parse(URL url) throws IOException {
        parse(url.openConnection().getInputStream());
    }

    /**
     * @param inputStream the {@code InputStream} of the Cellosaurus file
     * @throws IOException if an I/O exception occurs
     */
    void parse(InputStream inputStream) throws IOException;
}

package org.expasy.cellosaurus.formats;

import org.expasy.cellosaurus.formats.json.JsonFormatter;
import org.expasy.cellosaurus.wrappers.Search;

import java.io.IOException;

/**
 * Interface regulating the key behaviours of format writers
 */
public interface Writer {

    /**
     * @param json the relevant search information as a JSON {@code String}
     * @throws IOException if an I/O exception occurs
     */
    default void add(String json) throws IOException {
        add(new JsonFormatter().toSearch(json));
    }

    /**
     * @param search the relevant search information as a {@code Search} object
     * @throws IOException if an I/O exception occurs
     */
    void add(Search search) throws IOException;

    /**
     * @throws IOException if an I/O exception occurs
     */
    void write() throws IOException;

    void close();
}

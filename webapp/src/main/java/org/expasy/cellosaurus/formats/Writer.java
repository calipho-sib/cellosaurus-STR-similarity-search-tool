package org.expasy.cellosaurus.formats;

import java.io.IOException;

public interface Writer {

    void write() throws IOException;
    void close();
}

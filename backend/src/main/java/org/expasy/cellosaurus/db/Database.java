package org.expasy.cellosaurus.db;

import java.util.Objects;

public class Database {
    private String version;
    private String updated;
    private int cellLines;
    private int publications;

    public Database(String version, String updated, int cellLines, int publications) {
        this.version = version;
        this.updated = updated;
        this.cellLines = cellLines;
        this.publications = publications;
    }

    public String getVersion() {
        return version;
    }

    public String getUpdated() {
        return updated;
    }

    public int getCellLines() {
        return cellLines;
    }

    public int getPublications() {
        return publications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return cellLines == database.cellLines &&
                publications == database.publications &&
                version.equals(database.version) &&
                updated.equals(database.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, updated, cellLines, publications);
    }

    @Override
    public String toString() {
        return version;
    }
}

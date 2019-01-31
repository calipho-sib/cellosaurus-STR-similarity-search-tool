package org.expasy.cellosaurus.db;

import java.util.Objects;

public class Database {
    private String version;
    private String updated;
    private int cellLineCount;
    private int publicationCount;

    public Database(String version, String updated, int cellLineCount, int publicationCount) {
        this.version = version;
        this.updated = updated;
        this.cellLineCount = cellLineCount;
        this.publicationCount = publicationCount;
    }

    public String getVersion() {
        return version;
    }

    public String getUpdated() {
        return updated;
    }

    public int getCellLineCount() {
        return cellLineCount;
    }

    public int getPublicationCount() {
        return publicationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return cellLineCount == database.cellLineCount &&
                publicationCount == database.publicationCount &&
                version.equals(database.version) &&
                updated.equals(database.updated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, updated, cellLineCount, publicationCount);
    }

    @Override
    public String toString() {
        return version;
    }
}

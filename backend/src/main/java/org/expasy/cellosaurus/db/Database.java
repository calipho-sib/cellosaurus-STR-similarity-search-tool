package org.expasy.cellosaurus.db;

import java.util.Objects;

/**
 * Class representing a database. Its main purpose is to serve as a wrapper for metadata, allowing to track the version
 * of the STR profiles that serve as reference.
 */
public class Database {
    private final String version;
    private final String updated;
    private final int cellLineCount;
    private final int publicationCount;

    /**
     * Main constructor
     *
     * @param version          the version of the database
     * @param updated          the date of the database update
     * @param cellLineCount    the number of cell lines listed in the database
     * @param publicationCount the number of publications listed in the database
     */
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

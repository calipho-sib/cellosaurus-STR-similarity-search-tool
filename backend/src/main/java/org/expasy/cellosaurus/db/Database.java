package org.expasy.cellosaurus.db;

/**
 * Class representing a database. Its main purpose is to serve as a wrapper for metadata, allowing to track the version
 * of the STR profiles that serve as reference.
 */
public enum Database {
    CELLOSAURUS;

    private String version;
    private String updated;
    private int cellLineCount;
    private int publicationCount;

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

    public void setVersion(String version) {
        this.version = version;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setCellLineCount(int cellLineCount) {
        this.cellLineCount = cellLineCount;
    }

    public void setPublicationCount(int publicationCount) {
        this.publicationCount = publicationCount;
    }

    public String toJson() {
        return "{\"version\":\"" + version +
                "\",\"updated\":\"" + updated +
                "\",\"cellLineCount\":" + cellLineCount +
                ",\"publicationCount\":" + publicationCount +
                '}';
    }
}

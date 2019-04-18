package org.expasy.cellosaurus.genomics.str;

import java.util.Objects;

/**
 * Class representing a STR allele, defined as the number of repeats of the STR for a given STR marker. Its value is a
 * {@code String} since it needs to support "X" and "Y" for Amelogenin, "ND" for the undetected STR markers and the
 * variant alleles.
 */
public class Allele {
    private final String value;
    private Boolean matched;

    /**
     * Main constructor
     *
     * @param value the value of the allele, representing the number of repeats of the STR
     */
    public Allele(String value) {
        this.value = value;
        this.matched = false;
    }

    /**
     * Copy constructor
     *
     * @param that another instance of {@code Allele}
     */
    public Allele(Allele that) {
        this.value = that.value;
        this.matched = that.matched;
    }

    public String getValue() {
        return value;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public Boolean getMatched() {
        return matched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Allele allele = (Allele) o;
        return value.equals(allele.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

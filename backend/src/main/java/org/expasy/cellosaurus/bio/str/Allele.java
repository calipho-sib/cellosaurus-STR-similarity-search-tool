package org.expasy.cellosaurus.bio.str;

import java.util.Objects;

public class Allele {
    private String value;
    private Boolean matched;

    public Allele(String value) {
        this.value = value;
    }

    public Allele(Allele that) {
        this.value = that.value;
        this.matched = that.matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
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

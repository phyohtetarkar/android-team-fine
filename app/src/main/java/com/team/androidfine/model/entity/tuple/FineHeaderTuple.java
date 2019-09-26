package com.team.androidfine.model.entity.tuple;

import java.util.Objects;

public class FineHeaderTuple implements Fine {

    private String header;

    public FineHeaderTuple(String header) {
        this.header = header;
    }

    @Override
    public Object getIdentity() {
        return header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FineHeaderTuple that = (FineHeaderTuple) o;
        return Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }
}

package com.team.androidfine.model.entity.tuple;

public class FineHeaderTuple implements Fine{

    private String header;

    public FineHeaderTuple(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

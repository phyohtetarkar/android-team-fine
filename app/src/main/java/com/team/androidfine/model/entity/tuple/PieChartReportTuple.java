package com.team.androidfine.model.entity.tuple;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PieChartReportTuple {

    @JsonProperty("name")
    private String member;
    private int amount;

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

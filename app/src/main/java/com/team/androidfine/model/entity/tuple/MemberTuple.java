package com.team.androidfine.model.entity.tuple;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MemberTuple {

    @JsonProperty("id")
    private int memberId;
    private String name;
    private String photo;
    @JsonProperty("fine")
    private int totalFine;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(int totalFine) {
        this.totalFine = totalFine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberTuple that = (MemberTuple) o;
        return memberId == that.memberId &&
                totalFine == that.totalFine &&
                Objects.equals(name, that.name) &&
                Objects.equals(photo, that.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, name, photo, totalFine);
    }
}

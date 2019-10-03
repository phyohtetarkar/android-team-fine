package com.team.androidfine.model.entity.tuple;

import androidx.room.Ignore;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Objects;

public class FineTuple implements Fine {

    @JsonProperty("fineId")
    private long id;
    private int fine;
    private String title;
    @JsonProperty("name")
    private String member;
    @JsonProperty("date")
    private long timestamp;
    private int memberId;

    @Ignore
    private LocalDate localDate;

    public FineTuple() {
    }

    public String getFormatDate() {
        if (localDate == null) {
            localDate = LocalDate.fromDateFields(new Date(timestamp));
        }
        return localDate.toString("MMM dd, yyyy");
    }

    @Override
    public Object getIdentity() {
        return id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FineTuple fineTuple = (FineTuple) o;
        return id == fineTuple.id &&
                fine == fineTuple.fine &&
                timestamp == fineTuple.timestamp &&
                memberId == fineTuple.memberId &&
                Objects.equals(title, fineTuple.title) &&
                Objects.equals(member, fineTuple.member) &&
                Objects.equals(localDate, fineTuple.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fine, title, member, timestamp, memberId, localDate);
    }
}

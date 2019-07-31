package com.team.androidfine.model.entity.tuple;

import androidx.room.Ignore;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.Objects;

public class FineTuple {

    private long id;
    private int fine;
    private String title;
    private String member;
    private long timestamp;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FineTuple fineTuple = (FineTuple) o;
        return id == fineTuple.id &&
                fine == fineTuple.fine &&
                Objects.equals(title, fineTuple.title) &&
                Objects.equals(member, fineTuple.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fine, title, member);
    }
}

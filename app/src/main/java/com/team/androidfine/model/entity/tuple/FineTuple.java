package com.team.androidfine.model.entity.tuple;

import androidx.room.Embedded;
import androidx.room.Ignore;

import com.team.androidfine.model.entity.MemberFineId;

import org.joda.time.LocalDate;

import java.util.Objects;

public class FineTuple {

    @Embedded
    private MemberFineId id;
    private int fine;
    private String title;
    private String member;

    @Ignore
    private LocalDate localDate;

    public FineTuple() {
        id = new MemberFineId();
    }

    public String getFormatDate() {
        if (localDate == null) {
            localDate = LocalDate.parse(id.getDate());
        }
        return localDate.toString("MMM dd, yyyy");
    }

    public LocalDate getLocalDate() {
        if (localDate == null) {
            localDate = LocalDate.parse(id.getDate());
        }
        return localDate;
    }

    public MemberFineId getId() {
        return id;
    }

    public void setId(MemberFineId id) {
        this.id = id;
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
        return fine == fineTuple.fine &&
                Objects.equals(id, fineTuple.id) &&
                Objects.equals(title, fineTuple.title) &&
                Objects.equals(member, fineTuple.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fine, title, member);
    }
}

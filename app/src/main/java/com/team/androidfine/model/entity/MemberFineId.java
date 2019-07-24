package com.team.androidfine.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.util.Objects;

public class MemberFineId implements Serializable {

    @ColumnInfo(name = "member_id")
    private int memberId;
    @NonNull
    private String date;

    public MemberFineId() {
        this.date = "";
    }

    public MemberFineId(int memberId, String date) {
        this.memberId = memberId;
        this.date = date;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberFineId that = (MemberFineId) o;
        return memberId == that.memberId &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, date);
    }
}

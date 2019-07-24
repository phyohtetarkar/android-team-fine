package com.team.androidfine.model.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(tableName = "member_fine", primaryKeys = {"member_id", "date"})
public class MemberFine extends BaseObservable {

    @NonNull
    @Embedded
    private MemberFineId id;
    private String title;
    private int fine;

    public MemberFine() {
        id = new MemberFineId();
    }

    public MemberFineId getId() {
        return id;
    }

    public void setId(MemberFineId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}

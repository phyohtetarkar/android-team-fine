package com.team.androidfine.model.entity;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import org.joda.time.DateTime;

@Entity(tableName = "member_fine",
        primaryKeys = {"member_id", "date"},
        foreignKeys = {
            @ForeignKey(
                    entity = Member.class,
                    parentColumns = "id",
                    childColumns = "member_id"
            )
        }
)
public class MemberFine extends BaseObservable {

    @NonNull
    @Embedded
    private MemberFineId id;
    private String title;
    private int fine;
    private long timestamp;

    public MemberFine() {
        id = new MemberFineId();
        timestamp = DateTime.now().getMillis();
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

package com.team.androidfine.model.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.joda.time.DateTime;

@Entity(tableName = "member_fine",
        foreignKeys = {
                @ForeignKey(
                        entity = Member.class,
                        parentColumns = "id",
                        childColumns = "member_id"
                )
        }
)
public class MemberFine extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private int fine;
    private long timestamp;
    @ColumnInfo(name = "member_id")
    private int memberId;


    public MemberFine() {
        timestamp = DateTime.now().getMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
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

package com.team.androidfine.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.team.androidfine.model.dao.MemberDao;
import com.team.androidfine.model.dao.MemberFineDao;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.MemberFine;

@Database(entities = {
        Member.class,
        MemberFine.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MemberDao memberDao();

    public abstract MemberFineDao memberFineDao();

}

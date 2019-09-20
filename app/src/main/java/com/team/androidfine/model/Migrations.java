package com.team.androidfine.model;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class Migrations {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'Category' ('id' INTEGER NOT NULL , 'name' TEXT ," +
                    "'value' INTEGER NOT NULL , PRIMARY KEY('id'))");
        }
    };
}

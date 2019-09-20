package com.team.androidfine;

import android.content.Context;

import androidx.room.Room;

import com.team.androidfine.model.AppDatabase;
import com.team.androidfine.model.Migrations;
import com.team.androidfine.model.repo.CategoryRepo;
import com.team.androidfine.model.repo.MemberFineRepo;
import com.team.androidfine.model.repo.MemberRepo;

public abstract class ServiceLocator {

    private static ServiceLocator instance;

    public static ServiceLocator getInstance(Context ctx) {
        if (instance == null) {
            instance = new DefaultServiceLocator(ctx);
        }
        return instance;
    }

    public abstract MemberRepo memberRepo();

    public abstract MemberFineRepo memberFineRepo();

    public abstract CategoryRepo categoryRepo();

    static class DefaultServiceLocator extends ServiceLocator {

        private AppDatabase database;

        private MemberRepo memberRepo;
        private MemberFineRepo memberFineRepo;
        private CategoryRepo categoryRepo;

        DefaultServiceLocator(Context ctx) {
            database = Room.databaseBuilder(ctx, AppDatabase.class, "android-fine")
                    .addMigrations(Migrations.MIGRATION_1_2)
                    .build();
        }

        @Override
        public MemberRepo memberRepo() {
            if (memberRepo == null) {
                memberRepo = new MemberRepo(database.memberDao());
            }
            return memberRepo;
        }

        @Override
        public MemberFineRepo memberFineRepo() {
            if (memberFineRepo == null) {
                memberFineRepo = new MemberFineRepo(database.memberFineDao());
            }
            return memberFineRepo;
        }

        @Override
        public CategoryRepo categoryRepo() {
            if (categoryRepo == null) {
                categoryRepo = new CategoryRepo(database.categoryDao());
            }
            return categoryRepo;
        }

    }
}

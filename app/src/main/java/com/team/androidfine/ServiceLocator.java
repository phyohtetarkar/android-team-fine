package com.team.androidfine;

import android.content.Context;

import androidx.room.Room;

import com.team.androidfine.model.AppDatabase;
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

    static class DefaultServiceLocator extends ServiceLocator {

        private AppDatabase database;

        private MemberRepo memberRepo;
        private MemberFineRepo memberFineRepo;

        DefaultServiceLocator(Context ctx) {
            database = Room.databaseBuilder(ctx, AppDatabase.class, "android-fine").build();
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

    }
}

package com.team.androidfine;

import android.content.Context;

import androidx.room.Room;

import com.team.androidfine.model.AppDatabase;
import com.team.androidfine.model.Migrations;
import com.team.androidfine.model.api.CategoryAPI;
import com.team.androidfine.model.api.MemberAPI;
import com.team.androidfine.model.api.MemberFineAPI;
import com.team.androidfine.model.repo.CategoryRepo;
import com.team.androidfine.model.repo.MemberFineRepo;
import com.team.androidfine.model.repo.MemberRepo;
import com.team.androidfine.model.repo.remote.CategoryRepoImpl;
import com.team.androidfine.model.repo.remote.MemberFineRepoImpl;
import com.team.androidfine.model.repo.remote.MemberRepoImpl;
import com.team.androidfine.model.service.DatabaseBackupRestoreService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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

    public abstract DatabaseBackupRestoreService backupRestoreService();

    public abstract void closeDatabase();

    public abstract void openDatabase();

    static class DefaultServiceLocator extends ServiceLocator {

        private AppDatabase database;
        private Retrofit retrofit;
        private Context context;

        private MemberRepo memberRepo;
        private MemberFineRepo memberFineRepo;
        private CategoryRepo categoryRepo;
        private DatabaseBackupRestoreService backupRestoreService;

        DefaultServiceLocator(Context ctx) {
            this.context = ctx;
            openDatabase();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ctx.getString(R.string.server_path) + ":8080/android-fine/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
        }

        @Override
        public MemberRepo memberRepo() {
            if (memberRepo == null) {
               // memberRepo = new MemberRepoImpl(database.memberDao());
                memberRepo = new MemberRepoImpl(retrofit.create(MemberAPI.class));
            }
            return memberRepo;
        }

        @Override
        public MemberFineRepo memberFineRepo() {
            if (memberFineRepo == null) {
                //memberFineRepo = new MemberFineRepoImpl(database.memberFineDao());
                memberFineRepo = new MemberFineRepoImpl(retrofit.create(MemberFineAPI.class));
            }
            return memberFineRepo;
        }

        @Override
        public CategoryRepo categoryRepo() {
            if (categoryRepo == null) {
                //categoryRepo = new CategoryRepoImpl(database.categoryDao());
                categoryRepo = new CategoryRepoImpl(retrofit.create(CategoryAPI.class));
            }
            return categoryRepo;
        }

        @Override
        public DatabaseBackupRestoreService backupRestoreService() {
            if (backupRestoreService == null) {
                backupRestoreService = new DatabaseBackupRestoreService();
            }
            return backupRestoreService;
        }

        @Override
        public void closeDatabase() {
            memberRepo = null;
            memberFineRepo = null;
            categoryRepo = null;
            database.close();
        }

        @Override
        public void openDatabase() {
            database = Room.databaseBuilder(context, AppDatabase.class, "android-fine")
                    .addMigrations(Migrations.MIGRATION_1_2)
                    .build();
            memberRepo = null;
            memberFineRepo = null;
            categoryRepo = null;
        }

    }
}

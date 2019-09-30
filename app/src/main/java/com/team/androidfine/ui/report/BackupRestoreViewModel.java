package com.team.androidfine.ui.report;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.service.DatabaseBackupRestoreService;

import java.io.File;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class BackupRestoreViewModel extends AndroidViewModel {

    private DatabaseBackupRestoreService service;
    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<String> restoreBackupResult = new MutableLiveData<>();

    public BackupRestoreViewModel(@NonNull Application application) {
        super(application);
        service = ServiceLocator.getInstance(application).backupRestoreService();
    }


    void backup() {
        String fileName = "android-fine-" + System.currentTimeMillis() + ".fkp";
        File outFile = new File(Environment.getExternalStorageDirectory(), fileName);
        disposable.add(service.backup(outFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    restoreBackupResult.setValue("Backup success:");
                }, t -> {
                    restoreBackupResult.setValue(t.getMessage());
                }));
    }

    void restore(InputStream stream) {
        ServiceLocator.getInstance(getApplication()).closeDatabase();
        disposable.add(service.restore(stream)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    ServiceLocator.getInstance(getApplication()).openDatabase();
                    restoreBackupResult.setValue("Restore success");
                }, t -> {
                    ServiceLocator.getInstance(getApplication()).openDatabase();
                    restoreBackupResult.setValue(t.getMessage());
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

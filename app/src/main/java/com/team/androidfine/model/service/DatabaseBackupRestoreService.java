package com.team.androidfine.model.service;

import android.annotation.SuppressLint;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.reactivex.Completable;

public class DatabaseBackupRestoreService {

    public Completable backup(File outFile) {
        return Completable.create(source -> {
            try {
                @SuppressLint("SdCardPath")
                File dir = new File("/data/data/com.team.androidfine/databases");
                if (outFile != null && dir.exists() && dir.listFiles().length > 0) {

                    FileOutputStream fos = new FileOutputStream(outFile);
                    ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(fos));

                    for (File f : dir.listFiles()) {

                        FileInputStream fIn = new FileInputStream(f);
                        ZipEntry zipEntry = new ZipEntry(f.getName());
                        zOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int len = fIn.read(bytes);
                        while (len >= 0) {
                            zOut.write(bytes, 0, len);
                            len = fIn.read(bytes);
                        }
                        fIn.close();
                    }

                    zOut.close();
                    source.onComplete();
                } else {
                    source.onError(new FileNotFoundException());
                }
            } catch (Exception e) {
                source.onError(e);
            }

        });
    }

    public Completable restore(InputStream inputStream) {

        return Completable.create(source -> {
            try {
                @SuppressLint("SdCardPath")
                File dir = new File("/data/data/com.team.androidfine/databases");
                if (inputStream != null) {
                    ZipInputStream zIn = new ZipInputStream(inputStream);

                    byte[] bytes = new byte[1024];
                    ZipEntry entry = zIn.getNextEntry();
                    while (entry != null) {
                        if (!entry.getName().startsWith("android-fine")) {
                            continue;
                        }

                        FileOutputStream fOut = new FileOutputStream(new File(dir, entry.getName()), false);
                        int len = zIn.read(bytes);
                        while (len > 0) {
                            fOut.write(bytes, 0, len);
                            len = zIn.read(bytes);
                        }
                        entry = zIn.getNextEntry();
                        fOut.close();
                    }
                    zIn.close();
                    source.onComplete();
                } else {
                    source.onError(new FileNotFoundException());
                }
            } catch (Exception e) {
                e.printStackTrace();
                source.onError(e);
            }
        });
    }
}

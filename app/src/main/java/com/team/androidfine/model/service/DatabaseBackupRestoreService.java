package com.team.androidfine.model.service;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.team.androidfine.AndroidFineApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.reactivex.Completable;

@SuppressLint("SdCardPath")
public class DatabaseBackupRestoreService {

    private static final String DB_DIR = "database/";
    private static final String IMAGE_DIR = "images/";
    private static final String DB_PATH = "/data/data/com.team.androidfine/databases";

    public Completable backup(File outFile) {
        return Completable.create(source -> {
            try {
                File dbDir = new File(DB_PATH);
                File imageDir = AndroidFineApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                if (outFile != null) {

                    FileOutputStream fos = new FileOutputStream(outFile);
                    ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(fos));

                    if (dbDir.exists() && dbDir.listFiles().length > 0) {
                        for (File f : dbDir.listFiles()) {
                            addFileToZip(zOut, f, DB_DIR);
                        }
                    }

                    if (imageDir.exists() && imageDir.listFiles().length > 0) {
                        for (File f : imageDir.listFiles()) {
                            addFileToZip(zOut, f, IMAGE_DIR);
                        }
                    }

                    zOut.closeEntry();
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
                File dbDir = new File(DB_PATH);
                File imageDir = AndroidFineApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (inputStream != null) {
                    ZipInputStream zIn = new ZipInputStream(inputStream);

                    byte[] bytes = new byte[1024];
                    ZipEntry entry = zIn.getNextEntry();

                    while (entry != null) {
                        if (entry.getName().startsWith(DB_DIR)) {
                            writeFileInDir(zIn, dbDir, entry.getName().replace(DB_DIR, ""), bytes);
                        } else if (entry.getName().startsWith(IMAGE_DIR)) {
                            writeFileInDir(zIn, imageDir, entry.getName().replace(IMAGE_DIR, ""), bytes);
                        }
                        entry = zIn.getNextEntry();
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

    private void addFileToZip(ZipOutputStream zOut, File file, String rootDir) throws IOException {
        FileInputStream fIn = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(rootDir  + file.getName());
        zOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int len = fIn.read(bytes);
        while (len >= 0) {
            zOut.write(bytes, 0, len);
            len = fIn.read(bytes);
        }
        fIn.close();
    }

    private void writeFileInDir(ZipInputStream zIn, File dir, String name, byte[] bytes) throws IOException {
        if (dir == null) {
            return;
        }

        if (!dir.exists()) dir.mkdirs();

        if (DB_PATH.equals(dir.getAbsolutePath()) && !name.startsWith("android-fine")) {
            return;
        }

        FileOutputStream fOut = new FileOutputStream(new File(dir, name), false);
        int len = zIn.read(bytes);
        while (len > 0) {
            fOut.write(bytes, 0, len);
            len = zIn.read(bytes);
        }
        fOut.close();
    }
}

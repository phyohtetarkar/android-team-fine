package com.team.androidfine.ui.report;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.team.androidfine.R;
import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.service.DatabaseBackupRestoreService;
import com.team.androidfine.ui.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MoreFragment extends Fragment {

    private static final int PERMISSION_WRITE_STORAGE = 1;
    private static final int PERMISSION_READ_STORAGE = 2;
    private static final int RESTORE_FILE_REQUEST = 3;

    private CompositeDisposable disposable = new CompositeDisposable();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvReport = view.findViewById(R.id.tvReport);
        TextView tvBackUp = view.findViewById(R.id.tvBackup);
        TextView tvRestore = view.findViewById(R.id.tvRestore);

        tvReport.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_moreFragment_to_reportFragment);
        });

        tvBackUp.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_STORAGE);
                return;
            }
            executeBackup();
        });

        tvRestore.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_READ_STORAGE);
                return;
            }

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            startActivityForResult(intent, RESTORE_FILE_REQUEST);

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESTORE_FILE_REQUEST && resultCode == Activity.RESULT_OK && data.getData() != null) {
            Uri uri = data.getData();
            String file = uri.toString();
            if (file.endsWith(".fkp")) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Are u sure to restore?")
                        .setNegativeButton(android.R.string.cancel, (di, i) -> di.dismiss())
                        .setPositiveButton(android.R.string.ok, (di, i) -> {
                            try {
                                executeRestore(requireContext().getContentResolver().openInputStream(uri));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            di.dismiss();
                        })
                        .show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("More");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    executeBackup();
                }
                break;
            case PERMISSION_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, RESTORE_FILE_REQUEST);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
    }

    private void executeBackup() {
        DatabaseBackupRestoreService service = ServiceLocator.getInstance(requireContext()).backupRestoreService();
        String fileName = "android-fine-" + System.currentTimeMillis() + ".fkp";
        File outFile = new File(Environment.getExternalStorageDirectory(), fileName);
        disposable.add(service.backup(outFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Snackbar.make(getView(), "Backup success:" + outFile.getAbsolutePath(), Snackbar.LENGTH_LONG).show();
                }, t -> {
                    Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }));
    }

    private void executeRestore(InputStream stream) {
        DatabaseBackupRestoreService service = ServiceLocator.getInstance(requireContext()).backupRestoreService();
        disposable.add(service.restore(stream)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> ServiceLocator.getInstance(requireContext()).closeDatabase())
                .subscribe(() -> {
                    ServiceLocator.getInstance(requireContext()).openDatabase();
                    Snackbar.make(getView(), "Restore success", Snackbar.LENGTH_LONG).show();
                }, t -> {
                    ServiceLocator.getInstance(requireContext()).openDatabase();
                    Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG).show();
                }));
    }


}

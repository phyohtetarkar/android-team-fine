package com.team.androidfine.ui.report;

import android.app.Application;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.model.entity.tuple.PieChartReportTuple;
import com.team.androidfine.model.repo.MemberFineRepo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.joda.time.LocalDateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReportViewModel extends AndroidViewModel {

    private MemberFineRepo repo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<List<PieChartReportTuple>> pieLiveData = new MutableLiveData<>();
    final MutableLiveData<String> exportResult = new MutableLiveData<>();

    public ReportViewModel(@NonNull Application application) {
        super(application);
        this.repo = ServiceLocator.getInstance(application).memberFineRepo();
    }

    void findReports() {
        disposable.add(repo.findPieReport()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pieLiveData::setValue, t -> {
                    t.printStackTrace();
                }));
    }

    void exportCSV() {
        disposable.add(repo.findAllWithMember()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> {
                    try {
                        String fileName = "android-fine-" + LocalDateTime.now().toString("yyyyMMddhhmmss") + ".csv";
                        File outFile = new File(Environment.getExternalStorageDirectory(), fileName);
                        FileWriter writer = new FileWriter(outFile);
                        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("No.", "Date", "Name", "Fine"));
                        for (int i = 0; i < l.size(); i++) {
                            FineTuple tuple = l.get(i);
                            printer.printRecord(i + 1, tuple.getFormatDate(), tuple.getMember(), tuple.getFine());
                        }
                        printer.close();
                        exportResult.setValue("Export success:" + outFile.getAbsolutePath());
                    } catch (IOException e) {
                        exportResult.setValue("Export fail");
                    }
                }, t -> {
                    t.printStackTrace();
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

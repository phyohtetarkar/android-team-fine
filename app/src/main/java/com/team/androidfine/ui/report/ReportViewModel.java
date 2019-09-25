package com.team.androidfine.ui.report;

import android.app.Application;
import android.app.ListActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.tuple.PieChartReportTuple;
import com.team.androidfine.model.repo.MemberFineRepo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReportViewModel extends AndroidViewModel {

    private MemberFineRepo repo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<List<PieChartReportTuple>> pieLiveData = new MutableLiveData<>();

    public ReportViewModel(@NonNull Application application) {
        super(application);
        this.repo = ServiceLocator.getInstance(application).memberFineRepo();
    }

    void findReports() {
        disposable.add(repo.findPieReport()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pieLiveData::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

package com.team.androidfine.ui.fine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.model.repo.MemberFineRepo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemberFineViewModel extends AndroidViewModel {
    private MemberFineRepo repo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<List<FineTuple>> fines = new MutableLiveData<>();

    public MemberFineViewModel(@NonNull Application application) {
        super(application);
        this.repo = ServiceLocator.getInstance(application).memberFineRepo();
    }

    public void findAll() {
        disposable.add(repo.findAllWithMember()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fines::setValue));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

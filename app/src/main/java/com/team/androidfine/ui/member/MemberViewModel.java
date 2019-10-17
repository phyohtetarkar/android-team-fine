package com.team.androidfine.ui.member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.tuple.MemberTuple;
import com.team.androidfine.model.repo.MemberRepo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemberViewModel extends AndroidViewModel {

    private MemberRepo repo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<List<MemberTuple>> members = new MutableLiveData<>();
    final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MemberViewModel(@NonNull Application application) {
        super(application);
        this.repo = ServiceLocator.getInstance(application).memberRepo();
    }

    public void findAll() {
        disposable.add(repo.findAllWithFine()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(members::setValue, t -> {
                    errorMessage.setValue(t.getMessage());
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

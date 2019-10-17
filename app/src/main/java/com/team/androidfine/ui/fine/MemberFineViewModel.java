package com.team.androidfine.ui.fine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.repo.MemberFineRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemberFineViewModel extends AndroidViewModel {
    private MemberFineRepo repo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<PagedList<Fine>> fines = new MutableLiveData<>();
    final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MemberFineViewModel(@NonNull Application application) {
        super(application);
        this.repo = ServiceLocator.getInstance(application).memberFineRepo();
    }

    public void insert(MemberFine fine) {
        disposable.add(repo.insert(fine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::findAll, t -> {
                    t.printStackTrace();
                }));
    }

    public void findAll() {
        disposable.add(repo.findAllWithMemberPageable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fines::setValue, t -> {
                    errorMessage.setValue(t.getMessage());
                }));
    }

    public void delete(long id) {
        disposable.add(repo.deleteById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    deleteResult.setValue(true);
                    findAll();
                }, t -> {
                    deleteResult.setValue(false);
                    findAll();
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

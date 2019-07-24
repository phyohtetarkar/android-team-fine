package com.team.androidfine.ui.member;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.repo.MemberRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemberEditViewModel extends AndroidViewModel {
    private MemberRepo repo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<Boolean> saveResult = new MutableLiveData<>();

    public final MutableLiveData<Member> member = new MutableLiveData<>();

    public MemberEditViewModel(@NonNull Application application) {
        super(application);
        this.repo = ServiceLocator.getInstance(application).memberRepo();
    }

    void save() {
        disposable.add(repo.save(member.getValue())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                saveResult.setValue(true);
            }, t -> {
                saveResult.setValue(false);
            }));
    }

    void delete() {
        disposable.add(repo.delete(member.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    saveResult.setValue(true);
                }, t -> {
                    saveResult.setValue(false);
                }));
    }

    void findMember(int id) {
        disposable.add(repo.findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(member::setValue, t -> member.setValue(new Member())));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

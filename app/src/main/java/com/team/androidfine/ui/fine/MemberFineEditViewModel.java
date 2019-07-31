package com.team.androidfine.ui.fine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.BR;
import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.repo.MemberFineRepo;
import com.team.androidfine.model.repo.MemberRepo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MemberFineEditViewModel extends AndroidViewModel {
    private MemberFineRepo memberFineRepo;
    private MemberRepo memberRepo;

    final CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<Boolean> saveResult = new MutableLiveData<>();
    final MutableLiveData<List<Member>> members = new MutableLiveData<>();

    public final MutableLiveData<FineType> fineType = new MutableLiveData<>();
    public final MutableLiveData<MemberFine> memberFine = new MutableLiveData<>();
    public final MutableLiveData<Member> member = new MutableLiveData<>();

    public MemberFineEditViewModel(@NonNull Application application) {
        super(application);
        this.memberFineRepo = ServiceLocator.getInstance(application).memberFineRepo();
        this.memberRepo = ServiceLocator.getInstance(application).memberRepo();
        this.fineType.setValue(FineType.BORE);
    }

    public void findMembers() {
        disposable.add(memberRepo.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(members::setValue));
    }

    void save() {
        MemberFine fine = memberFine.getValue();
        fine.setMemberId(member.getValue().getId());

        disposable.add(memberFineRepo.save(fine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    saveResult.setValue(true);
                }, t -> {
                    saveResult.setValue(false);
                }));
    }

    void delete() {
        MemberFine fine = memberFine.getValue();
        disposable.add(memberFineRepo.delete(fine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    saveResult.setValue(true);
                }, t -> {
                    saveResult.setValue(false);
                }));
    }


    void findById(long id) {
        disposable.add(memberFineRepo.fineById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(fine -> findMember(fine.getMemberId()))
                .subscribe(memberFine::setValue, t -> {
                    memberFine.setValue(new MemberFine());
                    findMembers();
                }));
    }

    public void addFine() {
        MemberFine fine = memberFine.getValue();
        fine.setFine(calcFine(v -> fine.getFine() + v));
        fine.notifyPropertyChanged(BR.fine);
    }

    public void minusFine() {
        MemberFine fine = memberFine.getValue();
        fine.setFine(calcFine(v -> fine.getFine() - v));
        fine.notifyPropertyChanged(BR.fine);
    }

    private void findMember(int id) {
        disposable.add(memberRepo.findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(member -> findMembers())
                .subscribe(member::setValue));
    }

    private int calcFine(Function<Integer, Integer> func) {
        FineType type = fineType.getValue();
        switch (type) {
            case BORE:
                return func.apply(100);
            case SLEEPING:
                return func.apply(1000);
            case LATE:
                return func.apply(1000);
        }
        return 1;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

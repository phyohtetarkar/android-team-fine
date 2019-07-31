package com.team.androidfine.model.repo;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.androidfine.model.dao.MemberFineDao;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.FineTuple;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MemberFineRepo {

    private MemberFineDao dao;

    public MemberFineRepo(MemberFineDao dao) {
        this.dao = dao;
    }

    public Completable save(MemberFine fine) {
        if (fine.getId() > 0) {
            return dao.update(fine);
        }

        return dao.insert(fine);
    }

    public Completable delete(MemberFine fine) {
        return dao.delete(fine);
    }

    public Single<MemberFine> fineById(long id) {
        return dao.findById(id);
    }

    public Flowable<List<FineTuple>> findAllWithMember() {
        return dao.findAllWithMember();
    }

    public Flowable<PagedList<FineTuple>> findAllWithMemberPageable() {
        Flowable<PagedList<FineTuple>> fineList =
                new RxPagedListBuilder<>(dao.findAllWithMemberPageable(), 25).buildFlowable(BackpressureStrategy.BUFFER);
        return fineList;
    }
}

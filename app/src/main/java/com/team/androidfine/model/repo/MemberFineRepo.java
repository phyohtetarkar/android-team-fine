package com.team.androidfine.model.repo;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.androidfine.model.dao.MemberFineDao;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.MemberFineId;
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
        return Completable.create(emitter -> {
            if (emitter.isDisposed()) {
                return;
            }

            try {
                if (dao.findCountById(fine.getId().getMemberId(), fine.getId().getDate()) > 0) {
                    MemberFine old = dao.findByIdSync(fine.getId().getMemberId(), fine.getId().getDate());
                    fine.setFine(fine.getFine() + old.getFine());
                    dao.updateSync(fine);
                } else {
                    dao.insertSync(fine);
                }
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    public Completable delete(MemberFine fine) {
        return dao.delete(fine);
    }

    public Single<MemberFine> fineById(MemberFineId id) {
        return dao.findById(id.getMemberId(), id.getDate());
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

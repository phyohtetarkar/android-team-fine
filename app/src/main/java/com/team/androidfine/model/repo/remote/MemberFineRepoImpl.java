package com.team.androidfine.model.repo.remote;

import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.team.androidfine.model.api.MemberFineAPI;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.model.entity.tuple.PieChartReportTuple;
import com.team.androidfine.model.repo.MemberFineRepo;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MemberFineRepoImpl implements MemberFineRepo {

    private MemberFineAPI api;

    public MemberFineRepoImpl(MemberFineAPI api) {
        this.api = api;
    }

    @Override
    public Completable save(MemberFine fine) {
        return Completable.create(source -> {
            api.save(fine).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onComplete();
                } else {
                    source.onError(new RuntimeException("Member fine cannot save."));
                }
            }, source::onError);
        });
    }

    @Override
    public Completable insert(MemberFine fine) {
        return save(fine);
    }

    @Override
    public Completable delete(MemberFine fine) {
        return deleteById(fine.getId());
    }

    @Override
    public Completable deleteById(long id) {
        return Completable.create(source -> {
            api.delete(id).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onComplete();
                } else {
                    source.onError(new RuntimeException("MemberFine cannot delete."));
                }
            }, source::onError);
        });
    }

    @Override
    public Single<MemberFine> fineById(long id) {
        return Single.create(source -> {
            api.findById(id).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onSuccess(resp.body());
                } else {
                    source.onError(new RuntimeException("MemberFine not found."));
                }
            }, source::onError);
        });
    }

    @Override
    public Flowable<List<FineTuple>> findAllWithMember() {
        return Flowable.create(source -> {
            api.findAll().blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onNext(resp.body());
                } else {
                    source.onError(new RuntimeException("MemberFines not found."));
                }
            }, source::onError);
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<PagedList<Fine>> findAllWithMemberPageable() {
        MemberFineDataSourceFactory factory = new MemberFineDataSourceFactory(api);
        return new RxPagedListBuilder<>(factory, 25).buildFlowable(BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<PieChartReportTuple>> findPieReport() {
        return Flowable.create(source -> {
            api.findReport().blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onNext(resp.body());
                } else {
                    source.onError(new RuntimeException("Report not found."));
                }
            }, source::onError);
        }, BackpressureStrategy.LATEST);
    }
}

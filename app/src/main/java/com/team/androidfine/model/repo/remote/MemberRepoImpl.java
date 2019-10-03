package com.team.androidfine.model.repo.remote;

import com.team.androidfine.model.api.MemberAPI;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.tuple.MemberTuple;
import com.team.androidfine.model.repo.MemberRepo;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MemberRepoImpl implements MemberRepo {

    private MemberAPI api;

    public MemberRepoImpl(MemberAPI api) {
        this.api = api;
    }

    @Override
    public Completable save(Member member) {
        return Completable.create(source -> {
            api.save(member).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onComplete();
                } else {
                    source.onError(new RuntimeException("Member cannot save."));
                }
            }, source::onError);
        });
    }

    @Override
    public Completable insert(Member member) {
        return save(member);
    }

    @Override
    public Completable delete(Member member) {
        return deleteById(member.getId());
    }

    @Override
    public Completable deleteById(int id) {
        return Completable.create(source -> {
            api.delete(id).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onComplete();
                } else {
                    source.onError(new RuntimeException("Member cannot delete."));
                }
            }, source::onError);
        });
    }

    @Override
    public Single<Member> findById(int id) {
        return Single.create(source -> {
            api.findById(id).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onSuccess(resp.body());
                } else {
                    source.onError(new RuntimeException("Member not found."));
                }
            },source::onError);
        });
    }

    @Override
    public Flowable<List<Member>> findAll() {
        return Flowable.create(source -> {
            api.findAll().blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onNext(resp.body());
                } else {
                    source.onError(new RuntimeException(""));
                }
            }, source::onError);
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<MemberTuple>> findAllWithFine() {
        return Flowable.create(source -> {
            api.findSummary().blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onNext(resp.body());
                } else {
                    source.onError(new RuntimeException(""));
                }
            }, source::onError);
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<String>> findImages() {
        return null;
    }
}

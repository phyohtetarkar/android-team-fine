package com.team.androidfine.model.repo;

import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.tuple.MemberTuple;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface MemberRepo {

    Completable save(Member member);

    Single<String> saveImage(File image);

    Completable insert(Member member);

    Completable delete(Member member);

    Completable deleteById(int id);

    Single<Member> findById(int id);

    Flowable<List<Member>> findAll();

    Flowable<List<MemberTuple>> findAllWithFine();

    Flowable<List<String>> findImages();
}

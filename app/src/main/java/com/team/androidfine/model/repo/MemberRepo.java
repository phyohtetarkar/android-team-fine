package com.team.androidfine.model.repo;

import androidx.lifecycle.LiveData;

import com.team.androidfine.model.dao.MemberDao;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.tuple.MemberTuple;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class MemberRepo {

    private MemberDao dao;

    public MemberRepo(MemberDao dao) {
        this.dao = dao;
    }

    public Completable save(Member member) {
        if (member.getId() > 0) {
            return dao.update(member);
        }

        return dao.insert(member);
    }

    public Completable insert(Member member) {
        return dao.insert(member);
    }

    public Completable delete(Member member) {
        return dao.delete(member);
    }

    public Completable deleteById(int id) {
        return dao.deleteById(id);
    }

    public Single<Member> findById(int id) {
        return dao.findById(id);
    }

    public Flowable<List<Member>> findAll() {
        return dao.findAll();
    }

    public Flowable<List<MemberTuple>> findAllWithFine() {
        return dao.findAllWithFine();
    }

}

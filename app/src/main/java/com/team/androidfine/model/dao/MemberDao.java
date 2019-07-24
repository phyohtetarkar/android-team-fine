package com.team.androidfine.model.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.tuple.MemberTuple;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface MemberDao extends CudDao<Member> {

    @Query("SELECT * FROM Member WHERE id = :id LIMIT 1")
    Member findByIdSync(int id);

    @Query("SELECT * FROM Member WHERE id = :id LIMIT 1")
    Single<Member> findById(int id);

    @Query("SELECT * FROM Member")
    Flowable<List<Member>> findAll();

    @Query("SELECT m.id as memberId, m.name , m.photo, SUM(mf.fine) as totalFine " +
            "FROM Member m LEFT JOIN member_fine mf " +
            "ON mf.member_id = m.id " +
            "GROUP BY m.id, m.name, m.photo")
    Flowable<List<MemberTuple>> findAllWithFine();

}

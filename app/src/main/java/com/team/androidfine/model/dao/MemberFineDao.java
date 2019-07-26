package com.team.androidfine.model.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.FineTuple;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface MemberFineDao extends CudDao<MemberFine> {

    @Query("SELECT * FROM member_fine WHERE member_id = :memberId AND date = :date LIMIT 1")
    Single<MemberFine> findById(int memberId, String date);

    @Query("SELECT * FROM member_fine")
    Flowable<List<MemberFine>> findAll();

    @Query("SELECT mf.member_id, mf.date , mf.fine, m.name as member, mf.title " +
            "FROM member_fine mf LEFT JOIN Member m " +
            "ON mf.member_id = m.id")
    Flowable<List<FineTuple>> findAllWithMember();

    @Query("SELECT mf.member_id, mf.date , mf.fine, m.name as member, mf.title " +
            "FROM member_fine mf LEFT JOIN Member m " +
            "ON mf.member_id = m.id " +
            "ORDER BY mf.timestamp DESC")
    DataSource.Factory<Integer, FineTuple> findAllWithMemberPageable();

}

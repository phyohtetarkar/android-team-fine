package com.team.androidfine.model.dao;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.model.entity.tuple.PieChartReportTuple;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface MemberFineDao extends CudDao<MemberFine> {

    @Query("SELECT * FROM member_fine WHERE id = :id LIMIT 1")
    Single<MemberFine> findById(long id);

    @Query("DELETE FROM MEMBER_FINE WHERE id = :id")
    Completable deleteById(long id);

    @Query("SELECT mf.id, mf.timestamp , mf.fine, m.name as member, mf.title,m.id as memberId " +
            "FROM member_fine mf LEFT JOIN Member m " +
            "ON mf.member_id = m.id")
    Flowable<List<FineTuple>> findAllWithMember();

    @Query("SELECT mf.id, mf.timestamp , mf.fine, m.name as member, mf.title,m.id as memberId " +
            "FROM member_fine mf LEFT JOIN Member m " +
            "ON mf.member_id = m.id " +
            "ORDER BY mf.timestamp DESC")
    DataSource.Factory<Integer, FineTuple> findAllWithMemberPageable();

    @Query("SELECT mf.id, mf.timestamp , mf.fine, m.name as member, mf.title, m.id as memberId " +
            "FROM member_fine mf LEFT JOIN Member m " +
            "ON mf.member_id = m.id " +
            "ORDER BY mf.timestamp DESC LIMIT 25 OFFSET :offSet")
    List<FineTuple> findAllWithMemberPageable(int offSet);

    @Query("SELECT m.name as member,SUM(mf.fine) as amount FROM MEMBER m " +
            "LEFT JOIN MEMBER_FINE mf ON m.id = mf.member_id " +
            "GROUP BY m.name")
    Flowable<List<PieChartReportTuple>> findPieReport();
}

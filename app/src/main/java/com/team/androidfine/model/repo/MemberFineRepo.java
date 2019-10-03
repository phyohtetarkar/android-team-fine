package com.team.androidfine.model.repo;

import androidx.paging.PagedList;

import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.model.entity.tuple.PieChartReportTuple;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface MemberFineRepo {

    Completable save(MemberFine fine);

    Completable insert(MemberFine fine);

    Completable delete(MemberFine fine);

    Completable deleteById(long id);

    Single<MemberFine> fineById(long id);

    Flowable<List<FineTuple>> findAllWithMember();

    Flowable<PagedList<Fine>> findAllWithMemberPageable();

    Flowable<List<PieChartReportTuple>> findPieReport();
}

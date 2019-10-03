package com.team.androidfine.model.repo.local;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.team.androidfine.model.dao.MemberFineDao;
import com.team.androidfine.model.entity.tuple.Fine;

public class MemberFineDataSourceFactory extends DataSource.Factory<Integer, Fine> {
    private MemberFineDao dao;

    public MemberFineDataSourceFactory(MemberFineDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public DataSource<Integer, Fine> create() {
        return new MemberFineDataSource(dao);
    }
}

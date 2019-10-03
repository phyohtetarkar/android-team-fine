package com.team.androidfine.model.repo.remote;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

import com.team.androidfine.model.api.MemberFineAPI;
import com.team.androidfine.model.entity.tuple.Fine;

public class MemberFineDataSourceFactory extends DataSource.Factory<Integer, Fine> {
    private MemberFineAPI api;

    public MemberFineDataSourceFactory(MemberFineAPI api) {
        this.api = api;
    }

    @NonNull
    @Override
    public DataSource<Integer, Fine> create() {
        return new MemberFineDataSource(api);
    }
}

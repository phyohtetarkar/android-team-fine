package com.team.androidfine.model.repo.remote;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.team.androidfine.model.api.MemberFineAPI;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineHeaderTuple;
import com.team.androidfine.model.entity.tuple.FineTuple;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberFineDataSource extends PageKeyedDataSource<Integer, Fine> {

    private MemberFineAPI api;

    public MemberFineDataSource(MemberFineAPI api) {
        this.api = api;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Fine> callback) {
        api.find(0, 25).blockingSubscribe(resp -> {
            if (resp.code() == 200) {
                callback.onResult(groupFines(resp.body()), 0, 1);
            }
        }, t -> {
            t.printStackTrace();
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Fine> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Fine> callback) {
        int offset = (params.key * 25) + 1;
        api.find(offset, 25).blockingSubscribe(resp -> {
            if (resp.code() == 200) {
                callback.onResult(groupFines(resp.body()), params.key + 1);
            }
        }, t -> t.printStackTrace());
    }

    private List<Fine> groupFines(List<FineTuple> list) {
        List<Fine> fines = new ArrayList<>();

        Map<LocalDate, List<Fine>> map = new HashMap<>();
        for (FineTuple ft : list) {
            ft.getFormatDate();
            if (map.containsKey(ft.getLocalDate())) {
                map.get(ft.getLocalDate()).add(ft);
            } else {
                List<Fine> tmp = new ArrayList<>();
                tmp.add(ft);
                map.put(ft.getLocalDate(), tmp);
            }
        }

        List<Map.Entry<LocalDate, List<Fine>>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, (f, l) -> l.getKey().compareTo(f.getKey()));

        for (Map.Entry<LocalDate, List<Fine>> en : entries) {
            fines.add(new FineHeaderTuple(en.getKey().toString("MMM dd, yyyy")));
            fines.addAll(en.getValue());
        }

        return fines;
    }
}

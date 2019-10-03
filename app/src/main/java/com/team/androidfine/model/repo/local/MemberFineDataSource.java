package com.team.androidfine.model.repo.local;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.team.androidfine.model.dao.MemberFineDao;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineHeaderTuple;
import com.team.androidfine.model.entity.tuple.FineTuple;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemberFineDataSource extends PageKeyedDataSource<Integer, Fine> {

    private MemberFineDao dao;

    public MemberFineDataSource(MemberFineDao dao) {
        this.dao = dao;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Fine> callback) {
        List<FineTuple> list = dao.findAllWithMemberPageable(0);
        callback.onResult(groupFines(list), 0, 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Fine> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Fine> callback) {
        int offset = (params.key * 25) + 1;
        List<FineTuple> list = dao.findAllWithMemberPageable(offset);
        callback.onResult(groupFines(list), params.key + 1);
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

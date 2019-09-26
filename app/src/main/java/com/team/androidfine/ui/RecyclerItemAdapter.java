package com.team.androidfine.ui;

import java.util.List;

public interface RecyclerItemAdapter<T> {

    void submitItems(List<T> list);

    void notifyItemRemovedAt(int pos);

    void notifyItemInsertedAt(int pos);

}

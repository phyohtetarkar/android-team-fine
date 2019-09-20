package com.team.androidfine.ui.category;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.ui.ListItemAdapter;

public class CategoryAdapter extends ListItemAdapter<Category> {

    private static final DiffUtil.ItemCallback<Category> DIFF_UTIL = new DiffUtil.ItemCallback<Category>() {
        @Override
        public boolean areItemsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Category oldItem, @NonNull Category newItem) {
            return oldItem.equals(newItem);
        }
    };



    public CategoryAdapter() {
        super(DIFF_UTIL);
    }

    @Override
    protected int layoutRes() {
        return R.layout.layout_category;
    }
}

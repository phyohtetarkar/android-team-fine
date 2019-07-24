package com.team.androidfine.ui.fine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.ui.ListItemAdapter;

public class MemberFineAdapter extends ListItemAdapter<FineTuple> {

    private static final DiffUtil.ItemCallback<FineTuple> DIFF_UTIL = new DiffUtil.ItemCallback<FineTuple>() {
        @Override
        public boolean areItemsTheSame(@NonNull FineTuple oldItem, @NonNull FineTuple newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FineTuple oldItem, @NonNull FineTuple newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MemberFineAdapter() {
        super(DIFF_UTIL);
    }

    @Override
    protected int layoutRes() {
        return R.layout.layout_member_fine;
    }
}

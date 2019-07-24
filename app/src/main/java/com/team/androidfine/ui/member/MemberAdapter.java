package com.team.androidfine.ui.member;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.tuple.MemberTuple;
import com.team.androidfine.ui.ListItemAdapter;

public class MemberAdapter extends ListItemAdapter<MemberTuple> {

    private static final DiffUtil.ItemCallback<MemberTuple> DIFF_UTIL = new DiffUtil.ItemCallback<MemberTuple>() {
        @Override
        public boolean areItemsTheSame(@NonNull MemberTuple oldItem, @NonNull MemberTuple newItem) {
            return oldItem.getMemberId() == newItem.getMemberId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MemberTuple oldItem, @NonNull MemberTuple newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MemberAdapter() {
        super(DIFF_UTIL);
    }

    @Override
    protected int layoutRes() {
        return R.layout.layout_member;
    }
}

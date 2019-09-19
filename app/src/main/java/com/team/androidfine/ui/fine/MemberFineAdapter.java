package com.team.androidfine.ui.fine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineHeaderTuple;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.ui.PagedListItemAdapter;

public class MemberFineAdapter extends PagedListItemAdapter<Fine> {

    private static final DiffUtil.ItemCallback<Fine> DIFF_UTIL = new DiffUtil.ItemCallback<Fine>() {
        @Override
        public boolean areItemsTheSame(@NonNull Fine oldItem, @NonNull Fine newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Fine oldItem, @NonNull Fine newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MemberFineAdapter() {
        super(DIFF_UTIL);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof FineHeaderTuple ? 0 : 1;
    }

    @Override
    protected int layoutRes(int viewType) {
        return viewType == 0 ? R.layout.layout_member_fine_header : R.layout.layout_member_fine;
    }
}

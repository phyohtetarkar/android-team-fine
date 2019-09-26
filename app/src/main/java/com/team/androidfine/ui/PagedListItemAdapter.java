package com.team.androidfine.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.team.androidfine.BR;

import java.util.List;

public abstract class PagedListItemAdapter<T> extends PagedListAdapter<T, PagedListItemAdapter.PagedListItemViewHolder> implements RecyclerItemAdapter<T> {

    private OnAdapterItemClickListener<T> onAdapterItemClickListener;

    @LayoutRes
    protected abstract int layoutRes(int viewType);

    public PagedListItemAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PagedListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutRes(viewType), parent, false);
        return new PagedListItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PagedListItemAdapter.PagedListItemViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener<T> onAdapterItemClickListener) {
        this.onAdapterItemClickListener = onAdapterItemClickListener;
    }

    public T getItemAt(int position) {
        return getItem(position);
    }

    @Override
    public void submitItems(List<T> list) {
        if (list instanceof PagedList) {
            super.submitList((PagedList<T>) list);
        }
    }

    @Override
    public void notifyItemInsertedAt(int pos) {
        super.notifyItemInserted(pos);
    }

    @Override
    public void notifyItemRemovedAt(int pos) {
        super.notifyItemRemoved(pos);
    }

    class PagedListItemViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding binding;

        PagedListItemViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                if (onAdapterItemClickListener != null) {
                    onAdapterItemClickListener.onClick(getItem(getAdapterPosition()));
                }
            });
        }

        <E> void bind(E obj) {
            binding.setVariable(BR.obj, obj);
        }
    }

}

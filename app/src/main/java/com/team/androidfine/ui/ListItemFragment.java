package com.team.androidfine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.team.androidfine.R;

public abstract class ListItemFragment<T> extends Fragment {

    private boolean started;
    protected boolean enableSwipeDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter());
        if (started) {
            recyclerView.setLayoutAnimation(null);
        }

        if (enableSwipeDelete) {
            SwipeDeleteCallback callback = new SwipeDeleteCallback(requireContext());
            ItemTouchHelper helper = new ItemTouchHelper(callback);
            helper.attachToRecyclerView(recyclerView);
            callback.setOnSwipeDeleteListener(this::onSwipeDelete);
        }

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> onNewClick());

        ViewTreeObserver observer = fab.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    new Handler().postDelayed(fab::show, 500);
                }
            });
        }
    }

    protected void showRecyclerViewAnimation() {
        if (getView() != null && !started) {
            RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
            recyclerView.scheduleLayoutAnimation();
            started = true;
        }
    }

    protected abstract RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter();

    protected abstract void onNewClick();

    protected void onSwipeDelete(int position) {
    }

}

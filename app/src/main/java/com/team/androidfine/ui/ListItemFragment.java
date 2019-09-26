package com.team.androidfine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.Consumer;
import androidx.core.util.Supplier;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.team.androidfine.R;
import com.team.androidfine.util.Worker;

public abstract class ListItemFragment<T> extends Fragment {

    private boolean started;
    protected boolean enableSwipeDelete;
    protected Worker deleteWorker;

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

    protected void invokeDeleteUndo(Supplier<T> supplier, Worker deleteFunc, Consumer<T> insertFunc) {
        T itemToDelete = supplier.get();
        deleteFunc.work();

        Snackbar snackbar = Snackbar.make(getView(), itemToDelete.getClass().getSimpleName() + " deleted!", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> {
            insertFunc.accept(itemToDelete);
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
        float scale = getResources().getDisplayMetrics().density;
        params.bottomMargin = (int) ((96 * scale) + 0.5f);
        snackbar.show();

    }

}

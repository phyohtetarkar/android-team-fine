package com.team.androidfine.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.team.androidfine.R;

public abstract class ListItemFragment<T> extends Fragment {

    private boolean started;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            onNewClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

}

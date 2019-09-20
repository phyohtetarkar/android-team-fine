package com.team.androidfine.ui.category;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.ui.ListItemFragment;
import com.team.androidfine.ui.MainActivity;

import java.util.List;

public class CategoryFragment extends ListItemFragment<Category> {

    private CategoryAdapter adapter;
    private CategoryViewModel viewModel;
    private Runnable deleteRunnable;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        viewModel.categories.observe(this, list -> {
            adapter.submitList(list);
            showRecyclerViewAnimation();
        });
        enableSwipeDelete = true;
        viewModel.deleteResult.observe(this, result -> {
            if (!result) {
                Snackbar.make(getView(), "Fail to delete category!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.findCategories();
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Categories");
        new Handler().postDelayed(viewModel::findCategories, 500);
    }

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        if (adapter == null) {
            adapter = new CategoryAdapter();
            adapter.setOnAdapterItemClickListener(category -> {
                showEdit(category.getId());
            });
        }
        return adapter;
    }

    @Override
    protected void onNewClick() {
        showEdit(null);
    }

    @Override
    protected void onSwipeDelete(int position) {
        deleteRunnable = () -> viewModel.delete(adapter.getItemAt(position));

        List<Category> categories = viewModel.categories.getValue();
        Category categoryToDelete = categories.get(position);
        categories.remove(position);
        adapter.submitList(categories);
        adapter.notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(getView(), "Category deleted!", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> {
            deleteRunnable = null;
            categories.add(position, categoryToDelete);
            adapter.submitList(categories);
            adapter.notifyItemInserted(position);
        });
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (deleteRunnable != null) {
                    deleteRunnable.run();
                    deleteRunnable = null;
                }
            }
        });
        snackbar.show();
    }

    private void showEdit(Integer id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        DialogFragment fragment = new CategoryEditFragment();
        if (id != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(CategoryEditFragment.KEY_CATEGORY_ID, id);
            fragment.setArguments(bundle);
        }

        fragment.show(ft, "dialog");
    }

}

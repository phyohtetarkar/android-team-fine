package com.team.androidfine.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.team.androidfine.databinding.CategoryEditBinding;

public class CategoryEditFragment extends DialogFragment {

    static final String KEY_CATEGORY_ID = "category_id";

    private CategoryEditViewModel viewModel;
    private CategoryEditBinding binding;

    interface SaveCompleteDelegate {
        void onComplete();
    }

    private SaveCompleteDelegate saveCompleteDelegate;

    public void setSaveCompleteDelegate(SaveCompleteDelegate saveCompleteDelegate) {
        this.saveCompleteDelegate = saveCompleteDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(CategoryEditViewModel.class);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CategoryEditBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setVm(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCancel.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.saveResult.observe(this, result -> {
            if (result) {
                if (saveCompleteDelegate != null) saveCompleteDelegate.onComplete();
                dismiss();
            }
        });
        int id = getArguments() != null ? getArguments().getInt(KEY_CATEGORY_ID) : 0;
        viewModel.findById(id);
    }
}

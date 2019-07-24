package com.team.androidfine.ui.fine;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.ui.ListItemAdapter;
import com.team.androidfine.ui.ListItemFragment;
import com.team.androidfine.ui.MainActivity;

import java.util.Collections;

public class MemberFineFragment extends ListItemFragment<FineTuple> {

    private MemberFineAdapter adapter;
    private MemberFineViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MemberFineAdapter();
        adapter.setOnAdapterItemClickListener(fine -> {
            Bundle args = new Bundle();
            args.putSerializable(MemberFineEditFragment.KEY_MEMBER_FINE_ID, fine.getId());
            Navigation.findNavController(getView()).navigate(R.id.action_memberFineFragment_to_memberFineEditFragment, args);
        });

        viewModel = ViewModelProviders.of(this).get(MemberFineViewModel.class);
        viewModel.fines.observe(this, list -> {
            Collections.sort(list, (f, b) -> b.getLocalDate().compareTo(f.getLocalDate()));
            adapter.submitList(list);
            showRecyclerViewAnimation();
        });
        new Handler().postDelayed(viewModel::findAll, 500);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Fines");
    }

    @Override
    protected ListItemAdapter<FineTuple> adapter() {
        return adapter;
    }

    @Override
    protected void onNewClick() {
        Navigation.findNavController(getView()).navigate(R.id.action_memberFineFragment_to_memberFineEditFragment);
    }
}

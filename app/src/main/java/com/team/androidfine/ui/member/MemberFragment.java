package com.team.androidfine.ui.member;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.tuple.MemberTuple;
import com.team.androidfine.ui.ListItemAdapter;
import com.team.androidfine.ui.ListItemFragment;
import com.team.androidfine.ui.MainActivity;

import java.util.Collections;

public class MemberFragment extends ListItemFragment<MemberTuple> {

    private MemberAdapter adapter;
    private MemberViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new MemberAdapter();
        adapter.setOnAdapterItemClickListener(member -> {
            Bundle args = new Bundle();
            args.putInt(MemberEditFragment.KEY_MEMBER_ID, member.getMemberId());
            Navigation.findNavController(getView()).navigate(R.id.action_memberFragment_to_memberEditFragment, args);
        });

        viewModel = ViewModelProviders.of(this).get(MemberViewModel.class);
        viewModel.members.observe(this, list -> {
            Collections.sort(list, (f, b) -> Long.compare(b.getTotalFine(), f.getTotalFine()));
            adapter.submitList(list);
            showRecyclerViewAnimation();
        });
        new Handler().postDelayed(viewModel::findAll, 500);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Members");
    }

    @Override
    protected ListItemAdapter<MemberTuple> adapter() {
        return adapter;
    }

    @Override
    protected void onNewClick() {
        Navigation.findNavController(getView()).navigate(R.id.action_memberFragment_to_memberEditFragment);
    }
}

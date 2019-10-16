package com.team.androidfine.ui.member;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.team.androidfine.R;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.model.entity.tuple.MemberTuple;
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
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.nav_enter_from_left_anim)
                    .setExitAnim(R.anim.nav_exit_from_left_anim)
                    .setPopEnterAnim(R.anim.nav_pop_enter_from_left_anim)
                    .setPopExitAnim(R.anim.nav_pop_exit_from_left_anim)
                    .build();
            Navigation.findNavController(getView()).navigate(R.id.action_memberFragment_to_memberEditFragment, args,options);
        });

        viewModel = ViewModelProviders.of(this).get(MemberViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Members");
        viewModel.members.observe(this, list -> {
            Collections.sort(list, (f, b) -> Long.compare(b.getTotalFine(), f.getTotalFine()));
            adapter.submitList(list);
            showRecyclerViewAnimation();
            stopRefresh();
        });
        new Handler().postDelayed(viewModel::findAll, 500);
    }

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }


    @Override
    protected void onNewClick() {

        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.nav_enter_from_bottom_anim)
                .setPopExitAnim(R.anim.nav_pop_exit_to_bottom_anim)
                .build();
        Navigation.findNavController(getView()).navigate(R.id.action_memberFragment_to_memberEditFragment, null, options);
    }

    @Override
    protected void onSwipeRefresh() {
        viewModel.findAll();
    }
}

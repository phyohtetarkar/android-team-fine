package com.team.androidfine.ui.fine;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.team.androidfine.R;
import com.team.androidfine.model.entity.MemberFine;
import com.team.androidfine.model.entity.tuple.Fine;
import com.team.androidfine.model.entity.tuple.FineHeaderTuple;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.ui.ListItemFragment;
import com.team.androidfine.ui.MainActivity;

import java.util.List;

public class MemberFineFragment extends ListItemFragment<Fine> {

    private MemberFineAdapter adapter;
    private MemberFineViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MemberFineAdapter();
        adapter.setOnAdapterItemClickListener(fine -> {
            if (fine instanceof FineHeaderTuple) {
                return;
            }

            FineTuple fineTuple = (FineTuple) fine;

            Bundle args = new Bundle();
            args.putLong(MemberFineEditFragment.KEY_MEMBER_FINE_ID, fineTuple.getId());
            Navigation.findNavController(getView()).navigate(R.id.action_memberFineFragment_to_memberFineEditFragment, args);
        });

        viewModel = ViewModelProviders.of(this).get(MemberFineViewModel.class);
        viewModel.fines.observe(this, list -> {
            adapter.submitList(list);
            showRecyclerViewAnimation();
        });

        enableSwipeDelete = true;
        viewModel.deleteResult.observe(this, result -> {
            if (!result) {
                Snackbar.make(getView(), "Fail to delete fine!", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Fines");
        new Handler().postDelayed(viewModel::findAll, 500);
    }

    @Override
    protected void onSwipeDelete(int position) {
        invokeDeleteUndo(() -> adapter.getItemAt(position)
                , () -> {
                    Fine fine = adapter.getItemAt(position);
                    if (fine instanceof FineTuple) {
                        viewModel.delete(((FineTuple) fine).getId());
                        viewModel.findAll();
                    }
                }, fine -> {
                    if (fine instanceof FineTuple) {
                        FineTuple fineTuple = (FineTuple) fine;
                        MemberFine memberFine = new MemberFine();
                        memberFine.setFine(fineTuple.getFine());
                        memberFine.setId(fineTuple.getId());
                        memberFine.setMemberId(fineTuple.getMemberId());
                        memberFine.setTimestamp(fineTuple.getTimestamp());
                        memberFine.setTitle(fineTuple.getTitle());
                        viewModel.insert(memberFine);
                        viewModel.findAll();
                    }
                });
    }

    @Override
    protected RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter() {
        return adapter;
    }

    @Override
    protected void onNewClick() {
        Navigation.findNavController(getView()).navigate(R.id.action_memberFineFragment_to_memberFineEditFragment);
    }
}

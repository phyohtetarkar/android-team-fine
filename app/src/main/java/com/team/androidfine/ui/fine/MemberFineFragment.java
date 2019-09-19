package com.team.androidfine.ui.fine;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.team.androidfine.R;
import com.team.androidfine.model.entity.tuple.FineHeaderTuple;
import com.team.androidfine.model.entity.tuple.FineTuple;
import com.team.androidfine.ui.ListItemFragment;
import com.team.androidfine.ui.MainActivity;

public class MemberFineFragment extends ListItemFragment<FineTuple> {

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Fines");
        new Handler().postDelayed(viewModel::findAll, 500);
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

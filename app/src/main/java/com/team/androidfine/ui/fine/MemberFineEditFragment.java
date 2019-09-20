package com.team.androidfine.ui.fine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.team.androidfine.R;
import com.team.androidfine.databinding.MemberFineEditFragmentBinding;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.ui.MainActivity;

public class MemberFineEditFragment extends Fragment {

    static final String KEY_MEMBER_FINE_ID = "member_fine_id";

    private MemberFineEditViewModel viewModel;
    private ArrayAdapter<Member> memberAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        memberAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);

        MainActivity activity = (MainActivity) requireActivity();
        activity.switchToggle(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Edit Fine");

        viewModel = ViewModelProviders.of(this).get(MemberFineEditViewModel.class);
        viewModel.saveResult.observe(this, result -> {
            if (result) {
                Navigation.findNavController(getView()).popBackStack();
            } else {
                Toast.makeText(requireContext(), "Error occurred.", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.members.observe(this, list -> {
            memberAdapter.clear();
            memberAdapter.addAll(list);

            Member member = viewModel.member.getValue();
            if (getView() != null && member != null) {
                Spinner spinner = getView().findViewById(R.id.spinnerAuthors);
                spinner.setSelection(memberAdapter.getPosition(member));
            }
        });

        viewModel.categories.observe(this, list -> {
            View view = getView();
            if (view == null) return;
            ChipGroup categoryGroup = view.findViewById(R.id.categoryGroup);
            for (Category c : list) {
                Chip chip = new Chip(view.getContext());
                chip.setText(c.getName());
                chip.setTag(c);
                chip.setCheckable(true);
                categoryGroup.addView(chip);
            }

            if (list.size() > 0) {
                Chip chip = (Chip) categoryGroup.getChildAt(0);
                chip.setChecked(true);
            }
            categoryGroup.invalidate();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MemberFineEditFragmentBinding binding = MemberFineEditFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setVm(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerAuthors = view.findViewById(R.id.spinnerAuthors);
        spinnerAuthors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Member member = memberAdapter.getItem(i);
                viewModel.member.setValue(member);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerAuthors.setAdapter(memberAdapter);

        EditText edAuthor = view.findViewById(R.id.edAuthor);
        edAuthor.setOnKeyListener((v, i, keyEvent) -> true);
        edAuthor.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                spinnerAuthors.performClick();
            }
            return true;
        });

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            viewModel.delete();
        });

        ChipGroup chipGroup = view.findViewById(R.id.categoryGroup);
        chipGroup.setOnCheckedChangeListener((cg, i) -> {
            Chip chip = cg.findViewById(i);
            if (chip != null) {
                Category c = (Category) chip.getTag();
                viewModel.setValue(c.getValue());
            } else {
                viewModel.setValue(0);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).navigateUp();
                return true;
            case R.id.action_save:
                viewModel.save();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        long id = args != null ? args.getLong(KEY_MEMBER_FINE_ID) : 0;

        viewModel.findById(id);
        //new Handler().postDelayed(viewModel::findMembers, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.switchToggle(true);
        activity.hideKeyboard();
    }
}

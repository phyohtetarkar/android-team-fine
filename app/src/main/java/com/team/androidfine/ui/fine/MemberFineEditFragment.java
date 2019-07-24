package com.team.androidfine.ui.fine;

import android.os.Bundle;
import android.os.Handler;
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

import com.team.androidfine.R;
import com.team.androidfine.databinding.MemberFineEditFragmentBinding;
import com.team.androidfine.model.entity.Member;
import com.team.androidfine.model.entity.MemberFineId;
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

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((rg, id) -> {
            switch (id) {
                case R.id.radioBore:
                    viewModel.fineType.setValue(FineType.BORE);
                    break;
                case R.id.radioSleeping:
                    viewModel.fineType.setValue(FineType.SLEEPING);
                    break;
                case R.id.radioLate:
                    viewModel.fineType.setValue(FineType.LATE);
                    break;
            }
        });

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
        MemberFineId id = args != null ? (MemberFineId) args.getSerializable(KEY_MEMBER_FINE_ID) : new MemberFineId();

        viewModel.findById(id);
        new Handler().postDelayed(viewModel::findMembers, 500);
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

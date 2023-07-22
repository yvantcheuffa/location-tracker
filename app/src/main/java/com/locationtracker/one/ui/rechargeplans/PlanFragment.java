package com.locationtracker.one.ui.rechargeplans;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.locationtracker.one.databinding.FragmentPlanBinding;
import com.locationtracker.one.model.Plan;

import java.util.ArrayList;

public class PlanFragment extends Fragment {
    public static final String ARG_PLAN = "ARG_PLAN";
    FragmentPlanBinding binding;

    public static PlanFragment newInstance() {
        return new PlanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlanBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeMenu();
    }

    private void initializeMenu() {
        ArrayList<Plan> plans = (ArrayList<Plan>) getArguments().getSerializable(ARG_PLAN);
        PlanAdapter adapter = new PlanAdapter(requireContext(), plans);
        binding.planListView.setAdapter(adapter);
    }
}
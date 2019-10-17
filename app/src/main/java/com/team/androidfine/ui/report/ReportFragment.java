package com.team.androidfine.ui.report;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.team.androidfine.R;
import com.team.androidfine.ui.MainActivity;

public class ReportFragment extends Fragment {

    private PieChart pieChart;
    private BarChart barChart;
    private ReportViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Report");
        activity.switchToggle(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel.exportResult.observe(this, result -> {
            Snackbar.make(getView(), result, Snackbar.LENGTH_LONG).show();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_report, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.pieChart);
        pieChart.setNoDataTextColor(Color.DKGRAY);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(10f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraRightOffset(48f);
        pieChart.setExtraTopOffset(48f);
        pieChart.setExtraBottomOffset(48f);
        pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
        pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        pieChart.getLegend().setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);


        barChart = view.findViewById(R.id.barChart);
        barChart.setNoDataTextColor(Color.DKGRAY);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setTextSize(10f);
        barChart.setScaleEnabled(false);

        SwipeRefreshLayout layout = view.findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(() -> {
            viewModel.findReports();
        });
        layout.setColorSchemeColors(ColorTemplate.MATERIAL_COLORS);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.pieLiveData.observe(this, l -> {
            if (l.size() > 0) {
                PieData pieData = ChartDataHelper.toPieData(l);
                pieChart.setData(pieData);
                pieChart.setCenterText("Total\n" + pieData.getYValueSum());
                pieChart.invalidate();
                pieChart.animateXY(1000, 1000);

                BarData barData = ChartDataHelper.toBarData(l);
                barChart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return l.get((int) value).getMember();
                    }

                });
                barChart.setData(barData);
                barChart.invalidate();
                barChart.animateY(1000);
            } else {
                pieChart.setData(null);
                barChart.setData(null);
                pieChart.invalidate();
                barChart.invalidate();
            }
            stopRefresh();
        });
        viewModel.errorMessage.observe(this, msg -> {
            stopRefresh();
            Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
        });
        viewModel.findReports();
    }

    private void stopRefresh() {
        SwipeRefreshLayout layout = getView().findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(getView()).navigateUp();
            return true;
        }

        if (item.getItemId() == R.id.action_export_csv) {
            viewModel.exportCSV();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pieChart = null;
        barChart = null;
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.switchToggle(true);
    }
}

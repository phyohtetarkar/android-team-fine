package com.team.androidfine.ui.report;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.team.androidfine.R;
import com.team.androidfine.ui.MainActivity;

public class ReportFragment extends Fragment {

    private PieChart pieChart;
    private BarChart barChart;
    private ReportViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ReportViewModel.class);

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Report");
        activity.switchToggle(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.pieLiveData.observe(this, l -> {

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
        });


        viewModel.findReports();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(getView()).navigateUp();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.switchToggle(true);
    }
}

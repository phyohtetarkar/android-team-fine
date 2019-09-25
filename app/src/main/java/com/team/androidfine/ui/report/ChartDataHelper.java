package com.team.androidfine.ui.report;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.team.androidfine.model.entity.tuple.PieChartReportTuple;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartDataHelper {

    public static PieData toPieData(List<PieChartReportTuple> list) {

        List<PieEntry> entries = new ArrayList<>();
        for (PieChartReportTuple tuple : list) {
            entries.add(new PieEntry(tuple.getAmount(), tuple.getMember()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100f);
        dataSet.setValueLinePart1Length(0.4f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueTextSize(12f);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat df = new DecimalFormat("###,###.#");
                df.setRoundingMode(RoundingMode.CEILING);
                return df.format(value) + " %";
            }

        });

        return new PieData(dataSet);
    }

    public static BarData toBarData(List<PieChartReportTuple> list) {

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            PieChartReportTuple tuple = list.get(i);
            entries.add(new BarEntry(i, tuple.getAmount()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Members");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(10f);
        dataSet.setHighlightEnabled(false);

        return new BarData(dataSet);
    }
}

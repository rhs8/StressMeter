package com.rana_hoshyarsadeghi.stressmeter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ResultsFragment extends Fragment {

    private static final String CSV_NAME = "stress_timestamp.csv";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_results, container, false);

        LineChart chart = root.findViewById(R.id.chart);
        TableLayout table = root.findViewById(R.id.table_results);

        ArrayList<Entry> points = new ArrayList<>();
        ArrayList<String> rows = new ArrayList<>();

        try {
            File csv = new File(requireContext().getFilesDir(), CSV_NAME);
            if (!csv.exists()) {
                Toast.makeText(requireContext(), "No data yet. Make some selections first.", Toast.LENGTH_SHORT).show();
                return root;
            }

            BufferedReader br = new BufferedReader(new FileReader(csv));
            String line;
            boolean isHeader = true;
            int index = 0;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String ts = parts[0].trim();
                    String sc = parts[1].trim();
                    rows.add(ts + "," + sc);
                    float y = Float.parseFloat(sc);
                    points.add(new Entry(index, y));
                    index++;
                }
            }
            br.close();
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Error reading CSV: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        if (!points.isEmpty()) {
            LineDataSet dataSet = new LineDataSet(points, "Stress over time");
            dataSet.setColor(Color.BLUE);
            dataSet.setLineWidth(3f);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setCircleRadius(4f);
            dataSet.setDrawValues(false);
            dataSet.setDrawFilled(true);
            dataSet.setFillColor(Color.parseColor("#8899FF"));
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData data = new LineData(dataSet);
            chart.setData(data);
            chart.setExtraOffsets(12, 0, 12, 12);
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setLabelCount(points.size(), true);
            xAxis.setDrawGridLines(false);
            xAxis.setTextColor(Color.DKGRAY);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) value);
                }
            });

            YAxis left = chart.getAxisLeft();
            left.setAxisMinimum(0f);
            left.setAxisMaximum(16f);
            left.setGranularity(2f);
            left.setDrawGridLines(true);
            left.setGridColor(Color.LTGRAY);
            left.setTextColor(Color.DKGRAY);
            chart.getAxisRight().setEnabled(false);

            Description desc = new Description();
            desc.setText("");
            chart.setDescription(desc);
            chart.getLegend().setEnabled(false);
            chart.animateX(1200);
            chart.setExtraOffsets(10, 20, 10, 20);
            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setAxisMaximum(leftAxis.getAxisMaximum() + 2f);
            leftAxis.setAxisMinimum(0f);
            chart.getAxisRight().setEnabled(false);
            chart.invalidate();
        }

        if (!rows.isEmpty()) {
            for (String row : rows) {
                String[] parts = row.split(",");
                if (parts.length >= 2) {
                    TableRow tr = new TableRow(requireContext());
                    tr.setPadding(4, 4, 4, 4);

                    TextView tvTime = new TextView(requireContext());
                    tvTime.setText(parts[0]);
                    tvTime.setGravity(Gravity.CENTER);
                    tvTime.setTextColor(Color.BLACK);

                    TextView tvStress = new TextView(requireContext());
                    tvStress.setText(parts[1]);
                    tvStress.setGravity(Gravity.CENTER);
                    tvStress.setTextColor(Color.BLACK);

                    tr.addView(tvTime);
                    tr.addView(tvStress);
                    table.addView(tr);
                }
            }
        }

        return root;
    }
}


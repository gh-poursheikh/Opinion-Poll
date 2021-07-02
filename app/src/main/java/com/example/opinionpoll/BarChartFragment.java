package com.example.opinionpoll;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.opinionpoll.databinding.FragmentBarChartBinding;
import com.example.opinionpoll.model.Report;
import com.example.opinionpoll.utils.MyWebService;
import com.example.opinionpoll.utils.PageViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarChartFragment extends Fragment {

    private static final String TAG = BarChartFragment.class.getSimpleName();

    private PageViewModel pageViewModel;
    private FragmentBarChartBinding binding;

    private BarChart barChart;
    private Button refreshChart;
    private ArrayList<Report> reportList;

    public BarChartFragment() {
        // Required empty public constructor
    }

    public static BarChartFragment newInstance() {
        BarChartFragment fragment = new BarChartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);

        reportList = new ArrayList<>();
        pullReport();
        pageViewModel.setReport(reportList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBarChartBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        barChart = view.findViewById(R.id.bar_chart);
        refreshChart = view.findViewById(R.id.refresh_button_bar);

        refreshChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pullReport();
            }
        });

        pageViewModel.getReport().observe(getViewLifecycleOwner(), new Observer<ArrayList<Report>>() {
            @Override
            public void onChanged(ArrayList<Report> reports) {
                pullReport();
            }
        });

        return view;
    }

    public void plotBarChart() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        final ArrayList<String> barLabels = new ArrayList<>();

        for (int i = 0; i < reportList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, (float) (reportList.get(i).getProductAverage()));
            barEntry.setData(reportList.get(i).getProductName());
            barEntries.add(barEntry);
            barLabels.add(reportList.get(i).getProductName());

            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barLabels));
            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Toast.makeText(BarChartFragment.this.getContext(), e.getData().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Product Name");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barLabels));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setLabelRotationAngle(45f);

        barChart.animateY(1000);
        barChart.setData(barData);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        barChart.invalidate();
    }

    public void pullReport() {
        MyWebService webService = MyWebService.retrofit.create(MyWebService.class);
        Call<ArrayList<Report>> call = webService.getReport();
        call.enqueue(new Callback<ArrayList<Report>>() {
            @Override
            public void onResponse(Call<ArrayList<Report>> call, Response<ArrayList<Report>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Network Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    reportList = response.body();
                    plotBarChart();
                    Log.d(TAG, "onResponse from requestReport(): " + response.toString());
                } catch (Exception e) {
                    Log.d(TAG, "onResponse from requestReport(): " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Report>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
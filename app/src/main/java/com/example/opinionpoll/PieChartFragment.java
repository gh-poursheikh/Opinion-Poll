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

import com.example.opinionpoll.databinding.FragmentPieChartBinding;
import com.example.opinionpoll.model.Report;
import com.example.opinionpoll.utils.MyWebService;
import com.example.opinionpoll.utils.PageViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PieChartFragment extends Fragment {

    private static final String TAG = PieChartFragment.class.getSimpleName();

    private PageViewModel pageViewModel;
    private FragmentPieChartBinding binding;

    private PieChart pieChart;
    private Button refreshChart;
    private ArrayList<Report> reportList;

    public PieChartFragment() {
        // Required empty public constructor
    }

    public static PieChartFragment newInstance() {
        PieChartFragment fragment = new PieChartFragment();
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
        binding = FragmentPieChartBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        pieChart = view.findViewById(R.id.pie_chart);
        refreshChart = view.findViewById(R.id.refresh_button_pie);

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

    public void plotPieChart() {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < reportList.size(); i++) {
            PieEntry pieEntry = new PieEntry((float) (reportList.get(i).getProductAverage()), reportList.get(i).getProductName());
            pieEntry.setData(reportList.get(i).getProductName());
            pieEntries.add(pieEntry);

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    Toast.makeText(getContext(), e.getData().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Product Name");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData pieData = new PieData();
        pieData.addDataSet(pieDataSet);

        pieChart.animateXY(1000, 1000);
        pieChart.setData(pieData);
        pieChart.getDescription().setText("Products");
        pieChart.invalidate();
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
                    plotPieChart();
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
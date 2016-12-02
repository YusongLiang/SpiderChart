package com.felix.spiderchart.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.felix.spiderchart.R;
import com.felix.spiderchart.library.entity.SpiderDataSet;
import com.felix.spiderchart.library.widget.SpiderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private SpiderView spiderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        spiderView = (SpiderView) findViewById(R.id.spider_view);
    }

    private void initData() {
        SpiderDataSet dataSet = new SpiderDataSet();
        List<TreeMap<String, Double>> samples = new ArrayList<>();
        TreeMap<String, Double> sample = new TreeMap<>();
        sample.put("value01", 1.31);
        sample.put("value03", 4.24);
        sample.put("value04", 3.22);
        sample.put("value02", 2.0);
        sample.put("value05", 3.55);
        sample.put("value06", 0.76);
        samples.add(sample);

        TreeMap<String, Double> sample1 = new TreeMap<>();
        sample1.put("value01", 2.0);
        sample1.put("value03", 1.0);
        sample1.put("value04", 4.7);
        sample1.put("value02", 4.0);
        sample1.put("value05", 3.0);
        sample1.put("value06", 2.76);
        samples.add(sample1);

        TreeMap<String, Double> sample2 = new TreeMap<>();
        sample2.put("value01", 4.0);
        sample2.put("value03", 2.0);
        sample2.put("value04", 3.0);
        sample2.put("value02", 1.0);
        sample2.put("value05", 1.0);
        sample2.put("value06", 5.5);
        samples.add(sample2);

        TreeMap<String, Double> sample3 = new TreeMap<>();
        sample3.put("value01", 3.0);
        sample3.put("value03", 3.0);
        sample3.put("value04", 2.0);
        sample3.put("value02", 1.0);
        sample3.put("value05", 6.0);
        sample3.put("value06", 3.37);
        samples.add(sample3);

        dataSet.setSampleList(samples);
        spiderView.setDataSet(dataSet);
    }
}

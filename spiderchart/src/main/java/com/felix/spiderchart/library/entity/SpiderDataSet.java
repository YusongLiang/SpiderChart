package com.felix.spiderchart.library.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Felix
 */
public class SpiderDataSet {
    private List<TreeMap<String, Double>> mSampleList = new ArrayList<>();

    public SpiderDataSet() {
    }

    public SpiderDataSet(List<TreeMap<String, Double>> sampleList) {
        mSampleList = sampleList;
    }

    public List<TreeMap<String, Double>> getSampleList() {
        return mSampleList;
    }

    public void setSampleList(List<TreeMap<String, Double>> sampleList) {
        mSampleList = sampleList;
    }

    public String[] getItems() {
        Set<String> nameSet = new TreeSet<>();
        for (TreeMap<String, Double> sample : mSampleList) {
            nameSet.addAll(sample.keySet());
        }
        return nameSet.toArray(new String[nameSet.size()]);
    }
}

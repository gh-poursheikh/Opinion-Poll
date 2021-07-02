package com.example.opinionpoll.utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.opinionpoll.model.Report;

import java.util.ArrayList;

public class PageViewModel extends ViewModel {

    public static final String TAG = PageViewModel.class.getSimpleName();

    private MutableLiveData<ArrayList<Report>> mutableLiveData = new MutableLiveData<>();

    public void setReport(ArrayList<Report> list) {
        //mutableLiveData.setValue(list);
        mutableLiveData.postValue(list);
        Log.d(TAG, "setReport was called!");
    }

    public LiveData<ArrayList<Report>> getReport() {
        Log.d(TAG, "getReport was called!");
        return mutableLiveData;
    }
}
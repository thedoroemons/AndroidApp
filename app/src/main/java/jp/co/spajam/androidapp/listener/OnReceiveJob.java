package jp.co.spajam.androidapp.listener;

import java.util.ArrayList;

import jp.co.spajam.androidapp.data.Job;

public interface OnReceiveJob {
    public void onReceiveJob(ArrayList<Job> jobList);
}
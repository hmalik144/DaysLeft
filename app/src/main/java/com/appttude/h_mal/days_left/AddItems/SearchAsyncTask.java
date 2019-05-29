package com.appttude.h_mal.days_left.AddItems;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.appttude.h_mal.days_left.Abn.AbnListAdapter;
import com.appttude.h_mal.days_left.Abn.AbnObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromAbnJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromNameJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.makeHttpRequest;

public class SearchAsyncTask extends AsyncTask<URL,Void,String>{

    private Activity activity;
    private ListView listView;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private boolean abn;

    OnTaskCompleted onTaskCompleted;

    public interface OnTaskCompleted{
        void onTaskCompleted(String s);
    }

    public SearchAsyncTask(Activity activity, ListView listView, LinearLayout linearLayout, ProgressBar progressBar, boolean abn) {
        this.activity = activity;
        this.listView = listView;
        this.linearLayout = linearLayout;
        this.progressBar = progressBar;
        this.abn = abn;
    }

    public SearchAsyncTask(OnTaskCompleted onTaskCompleted) {
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    protected String doInBackground(URL... urls) {
        String jsonResponse = null;


        try {
            jsonResponse = makeHttpRequest(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return jsonResponse;
    }



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progressBar != null){
            progressBar.setVisibility(View.GONE);

            final List<AbnObject> abnObjectArrayList;

            if (s.contains("callback")){
                s = s.replace("callback(","");
                s.substring(0,s.length()-1);
            }

            Log.i("URL", "onPostExecute: " + s);

            if (abn){
                abnObjectArrayList = ExtractFeatureFromAbnJson(s);
            }else {
                abnObjectArrayList = ExtractFeatureFromNameJson(s);
            }


            if (abnObjectArrayList != null && abnObjectArrayList.size() > 0){
                linearLayout.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                Log.i("URL", "onPostExecute: executed");
                listView.setAdapter(new AbnListAdapter(activity,abnObjectArrayList));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AbnObject currentAbnObject = abnObjectArrayList.get(position);

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("AbnObject",currentAbnObject);
                        activity.setResult(Activity.RESULT_OK,returnIntent);
                        activity.finish();
                    }
                });
            }else if (abnObjectArrayList == null){
                linearLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        }else {
            onTaskCompleted.onTaskCompleted(s);
        }

    }

}

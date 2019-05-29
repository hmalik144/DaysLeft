package com.appttude.h_mal.days_left.AddItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;

import com.appttude.h_mal.days_left.Abn.AbnListAdapter;
import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Abn.SearchAbnRecords;
import com.appttude.h_mal.days_left.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromAbnJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromNameJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.makeHttpRequest;

public class SearchEmployerDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private String TAG = this.getClass().getSimpleName();

    private EditText searchText;
    private TabLayout tableLayout;
    private static Boolean abn;
    private SearchAbnRecords abnRecords;
    private ListView listView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        LayoutInflater Inflater = getActivity().getLayoutInflater();
        View view = Inflater.inflate(R.layout.dialog_search_employer, null);

        tableLayout = view.findViewById(R.id.tab_layout);
        tableLayout.addOnTabSelectedListener(tabSelectedListener);
        searchText = view.findViewById(R.id.search_edittext);

        abnRecords = new SearchAbnRecords();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setNegativeButton(android.R.string.cancel,this);
        builder.setPositiveButton(android.R.string.ok,this);
        builder.setTitle("Search via:");

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE){
            String searchTerm = searchText.getText().toString().trim();

            URL callUrl;

            if (abn){
                callUrl = abnRecords.searchViaAbn(searchTerm);
            }else {
                callUrl = abnRecords.searchViaName(searchTerm);
            }

            GetCompanies task = new GetCompanies(getContext(),listView);
            task.execute(callUrl);

        }else{
            dismiss();
        }
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.i(TAG, "onTabSelected: " + tab.getText());
            if (tab.getText() == "A.B.N"){
                searchText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                searchText.setHint("Search via A.B.N");
                searchText.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                searchText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                abn = true;
            }else{
                searchText.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                searchText.setHint("Search via Name");
                searchText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                searchText.setFilters(new InputFilter[] {});
                abn = false;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    static class GetCompanies extends AsyncTask<URL,Void,String> {

        private Context context;
        private ListView listView;

        public GetCompanies(Context context, ListView listView) {
            this.context = context;
            this.listView = listView;
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

            List<AbnObject> abnObjectArrayList;

            if (s.contains("callback")){
                s = s.substring(8,s.length());
            }

            if (abn){
                abnObjectArrayList = ExtractFeatureFromAbnJson(s);
            }else {
                abnObjectArrayList = ExtractFeatureFromNameJson(s);
            }

            if (abnObjectArrayList != null){
                listView.setAdapter(new AbnListAdapter(context,abnObjectArrayList));
            }
        }
    }
}
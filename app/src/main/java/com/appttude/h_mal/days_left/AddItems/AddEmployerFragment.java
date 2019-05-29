package com.appttude.h_mal.days_left.AddItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.appttude.h_mal.days_left.Abn.AbnListAdapter;
import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Abn.SearchAbnRecords;

import com.appttude.h_mal.days_left.R;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromAbnJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromNameJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.makeHttpRequest;


public class AddEmployerFragment extends Fragment {

    int selected;
    private SearchView searchView;
    private boolean abn;
    private ListView listView;
    private ProgressBar progressBar;
    private LinearLayout emptyView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_employer, container, false);

        listView = view.findViewById(R.id.list_view);
        progressBar = view.findViewById(R.id.spinning_pb);
        emptyView = view.findViewById(R.id.empty_list);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void createDialog(){
        final String[] grpname = {"A.B.N","Company Name"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Search by:")
                .setSingleChoiceItems(grpname, selected, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                searchView.setQueryHint("Search via A.B.N");
                                searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                                abn = true;
                                setupSearchbar(dialog);
                                return;
                            case 1:
                                searchView.setQueryHint("Search via Name");
                                searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                                abn = false;
                                setupSearchbar(dialog);
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
            }
        }).create().show();
    }

    private void setupSearchbar(DialogInterface dialog){
        searchView.requestFocus();
        dialog.dismiss();

    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            String searchTerm = query.trim();
            SearchAbnRecords abnRecords = new SearchAbnRecords();

            URL callUrl;

            if (abn){
                if (query.length() > 11){
                    searchTerm = searchTerm.substring(0,11);
                }
                callUrl = abnRecords.searchViaAbn(searchTerm);
            }else {
                callUrl = abnRecords.searchViaName(searchTerm);
            }

            Log.i("URL", "onQueryTextSubmit: " + callUrl.toString());

            SearchAsyncTask asyncTask = new SearchAsyncTask(getActivity(),listView,emptyView,progressBar,abn);
            asyncTask.execute(callUrl);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (abn){
                if(newText.length()>11){
                    System.out.println("Text character is more than 11");
                    searchView.setQuery(newText.substring(0,11), false);
                }
            }

            return false;
        }
    };


}

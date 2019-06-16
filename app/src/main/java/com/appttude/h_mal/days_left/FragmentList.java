package com.appttude.h_mal.days_left;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.AddItems.AddShiftActivity;
import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_ID;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.USER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.mDatabase;


public class FragmentList extends Fragment {

    private static String TAG = "FragmentList";

    private DatabaseReference reference;
    private ListView listView;
    private FireAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        reference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE);
        reference.keepSynced(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listView = view.findViewById(R.id.page_two_list);

        adapter = new FireAdapter(getActivity(), ShiftObject.class, R.layout.list_item, reference);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String refId = adapter.getId(position);
                Intent intent = new Intent(getActivity(), AddShiftActivity.class);
                intent.putExtra(SHIFT_ID,refId);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_list_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.app_bar_filter:
                sortData();
                return false;

            default:
                break;
        }

        return false;
    }

    private void sortData(){
        final String[] grpname = {"Name","Date Added","Date of shift"};
        int checkedItem = -1;



        AlertDialog.Builder alt_bld = new AlertDialog.Builder(getContext());
        alt_bld.setTitle("Sort by:");
        alt_bld.setSingleChoiceItems(grpname, checkedItem, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, final int item) {
                switch (item){
                    case 0:
                        Query q1 = reference.orderByChild("abnObject/companyName")
                                .equalTo("GREEN CLOUD NURSERY");

                        q1.orderByChild("shiftDate")
                                .equalTo("2019-04-12");

                        adapter = new FireAdapter(getActivity(), ShiftObject.class, R.layout.list_item, q1);
                        break;
                    case 1:
                        adapter = new FireAdapter(getActivity(), ShiftObject.class, R.layout.list_item, reference.orderByChild("dateTimeAdded"));
                        break;
                    case 2:
                        adapter = new FireAdapter(getActivity(), ShiftObject.class, R.layout.list_item, reference.orderByChild("shiftDate"));
                        break;
                    default:

                }
            listView.setAdapter(adapter);
            dialog.dismiss();
            }
        })
//                .setPositiveButton("Ascending", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
////                sortOrder = sortQuery[0] + " ASC";
//
//                dialog.dismiss();
//            }
//        }).setNegativeButton("Descending", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
////                sortOrder = sortQuery[0] + " DESC";
//
//                dialog.dismiss();
//            }
//        })
        ;
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    public static Date convertDate(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(s);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        finally {
            if (d == null){
                sdf.applyPattern("dd/MM/yyyy");

                try {
                    d = sdf.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return d;
    }
}

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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure you want to delete?");
                builder.setNegativeButton(android.R.string.no, null);
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo: delete from previously used if last entry
                        adapter.getRef(position).removeValue();
                    }
                });
                builder.create().show();

                return false;
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
                filterData();
                return false;

            case R.id.app_bar_soft:
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
                        Query q1 = reference.orderByChild("abnObject/companyName").equalTo("GREEN CLOUD NURSERY");
//                                .startAt("2018-04-12").endAt("2019-03-29");

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
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void filterData(){
        final String[] groupName = {"Name","Date Added","Shift Type"};
        int checkedItem = -1;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter by:");
        builder.setSingleChoiceItems(groupName, checkedItem, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, final int item) {
                dialog.dismiss();

                switch (item){
                    case 0:
                        final FilterDialogClass filterDialog = new FilterDialogClass(getContext(), 0, new FilterDialogClass.FilterDialogClickListener() {
                            @Override
                            public void applyDates(String arg1, String arg2) {

                            }

                            @Override
                            public void applyAbn(String abn) {
                                applyFilter(abn,null);
                            }
                        });
                        filterDialog.show();

                        break;
                    case 1:
                        final FilterDialogClass filterDialog2 = new FilterDialogClass(getContext(), 1, new FilterDialogClass.FilterDialogClickListener() {
                            @Override
                            public void applyDates(String arg1, String arg2) {
                                applyFilter(arg1, arg2);
                            }

                            @Override
                            public void applyAbn(String abn) {

                            }
                        });
                        filterDialog2.show();
                        break;
                    case 2:
                        AlertDialog.Builder typeDialog = new AlertDialog.Builder(getContext());
                        final String[] typeString = {"Hourly", "Piece Rate"};

                        typeDialog.setSingleChoiceItems(new String[]{"Hourly", "Piece Rate"}, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Query q1 = reference.orderByChild("taskObject/workType").equalTo(typeString[which]);

                                adapter = new FireAdapter(getActivity(), ShiftObject.class, R.layout.list_item, q1);
                                listView.setAdapter(adapter);
                            }
                        });
                        typeDialog.create().show();
                        break;
                    default:

                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void applyFilter(String arg1, String arg2){
        Query q1;
        if (arg2 == null){
            q1 = reference.orderByChild("abnObject/abn").equalTo(arg1);
        }else {
            q1 = reference.orderByChild("shiftDate").startAt(arg1).endAt(arg2);
        }

        adapter = new FireAdapter(getActivity(), ShiftObject.class, R.layout.list_item, q1);
        listView.setAdapter(adapter);
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

package com.appttude.h_mal.days_left.AddItems;


import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Abn.AbnListAdapter;
import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.EMPLOYERREQUEST;
import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.REQUEST;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.EMPLOYER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.USER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.mDatabase;

public class PreviouslyUsedItemsClass implements ValueEventListener {

    private ProgressBar progressBar;
    private AddShiftActivity activity;

    public PreviouslyUsedItemsClass(ProgressBar progressBar, AddShiftActivity activity) {
        this.progressBar = progressBar;
        this.activity = activity;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        final ArrayList<String> strings = new ArrayList<>();
        final ArrayList<AbnObject> abnObjectArrayList = new ArrayList<>();

        for (DataSnapshot currentItem : dataSnapshot.getChildren()){
            strings.add(currentItem.getKey());

        }

        DatabaseReference abnListRef = mDatabase.child(EMPLOYER_FIREBASE);
        abnListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot currentItem : dataSnapshot.getChildren()){
                    AbnObject currentAbnObject = currentItem.getValue(AbnObject.class);
                    abnObjectArrayList.add(currentAbnObject);
                }

                if (abnObjectArrayList.size() > 0){

                    LayoutInflater factory = LayoutInflater.from(activity);
                    final View dialogView = factory.inflate(R.layout.dialog_previous_abns_used, null);

                    ListView listView = dialogView.findViewById(R.id.list_item_list_dialog);
                    Button button = dialogView.findViewById(R.id.button_list_dialog);

                    AbnListAdapter abnListAdapter = new AbnListAdapter(activity,abnObjectArrayList);
                    listView.setAdapter(abnListAdapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setView(dialogView);

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity();
                            alertDialog.dismiss();
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            activity.abnObject = abnObjectArrayList.get(position);
                            activity.setEmployerCard();
                            alertDialog.dismiss();
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                }else {
                    startActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });



        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(activity, "Could not load ", Toast.LENGTH_SHORT).show();
    }

    private void startActivity(){
        Intent intent = new Intent(activity,AddItemActivity.class);
        intent.putExtra(REQUEST,EMPLOYERREQUEST);
        activity.startActivityForResult(intent,EMPLOYERREQUEST);
    }
}

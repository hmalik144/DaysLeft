package com.appttude.h_mal.days_left;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static com.appttude.h_mal.days_left.MainActivity.shiftObjectArrayList;
import static com.appttude.h_mal.days_left.MainActivity.shiftsMap;

public class CustomValueEventListener implements ValueEventListener {

    private Context context;
    private FragmentManager fragmentManager;
    private ProgressBar progressBar;

    public CustomValueEventListener(Context context, FragmentManager fragmentManager, ProgressBar progressBar) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.progressBar = progressBar;

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            shiftObjectArrayList.add(postSnapshot.getValue(ShiftObject.class));
            shiftsMap.put(postSnapshot.getKey(),postSnapshot.getValue(ShiftObject.class));
        }

        if (shiftObjectArrayList.size() > 0){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,new FragmentHome()).commit();
        }else {
            Toast.makeText(context, "List Empty", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        progressBar.setVisibility(View.GONE);
    }
}

package com.appttude.h_mal.days_left;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appttude.h_mal.days_left.Objects.CustomViewHolder;
import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.appttude.h_mal.days_left.R;
import com.google.firebase.database.DatabaseReference;

import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.USER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.mDatabase;


public class FragmentHome extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
//        recyclerView.setAdapter(new RecyclerViewAdapter(getContext()));

        DatabaseReference reference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE);
        reference.keepSynced(true);

        recyclerView.setAdapter(new FireRecyclerAdapter(ShiftObject.class,R.layout.item_one,RecyclerView.ViewHolder.class,reference,getContext()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setHasFixedSize(true);

        return view;
    }

}

package com.appttude.h_mal.days_left.Login;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.appttude.h_mal.days_left.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


public class FullscreenActivity extends AppCompatActivity {

    protected static FragmentManager fragmentManager;
    protected static FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        FrameLayout frameLayout = findViewById(R.id.container);

        auth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,new BlankFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentManager.getFragments().size() > 1){
            fragmentManager.popBackStack();
        }
    }
}

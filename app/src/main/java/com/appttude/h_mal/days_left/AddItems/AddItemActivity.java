package com.appttude.h_mal.days_left.AddItems;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.R;

import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.EMPLOYERREQUEST;
import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.REQUEST;
import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.TASK_CONSTANT;

public class AddItemActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        int requestCode = getIntent().getIntExtra(REQUEST,EMPLOYERREQUEST);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (requestCode == EMPLOYERREQUEST){
            fragmentTransaction.replace(R.id.container,new AddEmployerFragment()).addToBackStack("Employer").commit();
        }else {
            TaskObject taskObject = getIntent().getParcelableExtra(TASK_CONSTANT);
            Fragment addTaskFragment = new AddTaskFragment();
            if (taskObject != null){
                Bundle bundle = new Bundle();
                bundle.putParcelable(TASK_CONSTANT,taskObject);

                addTaskFragment.setArguments(bundle);
            }
            fragmentTransaction.replace(R.id.container,addTaskFragment).addToBackStack("Task").commit();
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        fragmentManager.addOnBackStackChangedListener(backStackChangedListener);
    }

    FragmentManager.OnBackStackChangedListener backStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {

        }
    };

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Leave?")
                    .setMessage("Are you sure you return to previous?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).create().show();
        }
    }
}

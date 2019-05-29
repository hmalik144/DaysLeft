package com.appttude.h_mal.days_left.AddItems;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.appttude.h_mal.days_left.Abn.AbnListAdapter;
import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.REQUEST;
import static com.appttude.h_mal.days_left.AddItems.AddShiftActivity.TASKREQUEST;

public class AssociatedTasksClass implements ValueEventListener {

    private AddShiftActivity activity;
    private ProgressBar progressBar;

    public AssociatedTasksClass(AddShiftActivity activity, ProgressBar progressBar) {
        this.activity = activity;
        this.progressBar = progressBar;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        final ArrayList<TaskObject> taskObjects = new ArrayList<>();

        for (DataSnapshot currentTask : dataSnapshot.getChildren()){
            taskObjects.add(currentTask.getValue(TaskObject.class));
        }

        if (taskObjects.size() > 0){

            LayoutInflater factory = LayoutInflater.from(activity);
            final View dialogView = factory.inflate(R.layout.dialog_previous_abns_used, null);

            ListView listView = dialogView.findViewById(R.id.list_item_list_dialog);
            Button button = dialogView.findViewById(R.id.button_list_dialog);

            DialogListAdapter dialogListAdapter = new DialogListAdapter(activity,taskObjects);
            listView.setAdapter(dialogListAdapter);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(dialogView);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    activity.taskObject = taskObjects.get(position);
                    activity.setTaskCard();
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

    }

    private void startActivity(){
        Intent intent = new Intent(activity,AddItemActivity.class);
        intent.putExtra(REQUEST,TASKREQUEST);
        activity.startActivityForResult(intent,TASKREQUEST);
    }
}

package com.appttude.h_mal.days_left.Global;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Abn.SearchAbnRecords;
import com.appttude.h_mal.days_left.AddItems.SearchAsyncTask;
import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.ExtractFeatureFromAbnJson;
import static com.appttude.h_mal.days_left.Abn.SearchAbnRecords.makeHttpRequest;

public class FirebaseClass {

    private static final String TAG = "FirebaseClass";

    public static final String USER_FIREBASE = "users";
    public static final String EMPLOYER_FIREBASE = "employers";
    public static final String SHIFT_FIREBASE = "shifts";
    public static final String TASK_FIREBASE = "taskList";

    public static final String SHIFT_ID = "shift_id";

    public static final String PIECE = "Piece Rate";
    public static final String HOURLY = "Hourly";

    public static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    public interface Response {
        void retrieveAbnStringList(List<String> abnList);
        void retrieveAbnObjectList(List<AbnObject> abnObjects);
        void retrieveTaskList(List<TaskObject> taskObjects);
    }

    public interface matchListener{
        void abnMatch(Boolean confirm);
    }

    public interface Complete{
        void taskCompleted(Boolean success);
    }

    public FirebaseClass() {
    }

//    public void uploadImage(String path, String name) {
//
//        if(filePath != null) {
//            final ProgressDialog progressDialog = new ProgressDialog(context);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            final StorageReference ref = storageReference.child("images/"+  auth.getCurrentUser().getUid() + "/" + path
//                            + "/" + name);
//
//            UploadTask uploadTask = ref.putFile(filePath);
//
//            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
//                            .getTotalByteCount());
//                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
//                }
//            }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//                    return ref.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        delegate.retrieveAbnStringList(task.getResult());
//                        progressDialog.dismiss();
//                        Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                        Log.i(context.getClass().getSimpleName(), "onComplete: uploaded Successful uri: " + task.getResult());
//                    } else {
//                        delegate.retrieveAbnStringList(null);
//                        progressDialog.dismiss();
//                        Toast.makeText(context, "Failed to upload", Toast.LENGTH_SHORT).show();
//                        Log.i(context.getClass().getSimpleName(), "onComplete: failed to get url");
//                    }
//                }
//            });
//
//        }
//    }

    public void PushAbnObject(final AbnObject abnObject, final TaskObject taskObject, final ProgressBar progressBar, final Activity activity){
        final DatabaseReference pushRef = mDatabase.child(EMPLOYER_FIREBASE);
        pushRef.keepSynced(true);

        pushRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                
                boolean exists = false;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(abnObject.getAbn())){
                        exists = true;
                        Log.i(TAG, "onDataChange: exists");
                    }
                }

                //pushing new abn to list
                if (!exists){
                    Log.i(TAG, "onDataChange: triggered true");
                    abnObject.setDateAdded(getDateTimeString());
                    abnObject.setAddedById(auth.getUid());

//                    if (abnObject.getFromAbnSearch()){
                        final SearchAbnRecords abnRecords = new SearchAbnRecords();

                        SearchAsyncTask searchAsyncTask = new SearchAsyncTask(new SearchAsyncTask.OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(String s) {
                                AbnObject retrievedAbn = null;

                                try {
                                    retrievedAbn = ExtractFeatureFromAbnJson(s).get(0);
                                }catch (Exception e){
                                    Log.e(TAG, "onTaskCompleted: ", e);
                                }finally {
                                    if (retrievedAbn != null){
                                        abnObject.setCompanyName(retrievedAbn.getCompanyName());
                                    }
                                    pushRef.child(abnObject.getAbn()).setValue(abnObject)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    PushTaskObject(abnObject.getAbn(), taskObject, new Complete() {
                                                        @Override
                                                        public void taskCompleted(Boolean success) {
                                                            if (success){
                                                                activity.finish();
                                                            }else {
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(activity, "Could not upload Task", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }else {
                                                    Log.i(TAG, "onComplete: " + task.getResult());

                                                }
                                            }
                                        });
                                }

                            }
                        });
                        searchAsyncTask.execute(abnRecords.searchViaAbn(abnObject.getAbn()));
//                    }else
//                        {
//                        pushRef.child(abnObject.getAbn()).setValue(abnObject)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Log.i(TAG, "onComplete: triggered");
//                                        if (task.isSuccessful()){
//                                            PushTaskObject(abnObject.getAbn(), taskObject, new Complete() {
//                                                @Override
//                                                public void taskCompleted(Boolean success) {
//                                                    if (success){
//                                                        activity.finish();
//                                                    }else {
//                                                        progressBar.setVisibility(View.GONE);
//                                                        Toast.makeText(activity, "Could not upload Task", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
//                                        }else {
//                                            Log.i(TAG, "onComplete: " + task.getResult());
//
//                                        }
//                                    }
//                                });
//                    }
                }else {
                    Log.i(TAG, "onDataChange: exists false");
                    PushTaskObject(abnObject.getAbn(), taskObject, new Complete() {
                        @Override
                        public void taskCompleted(Boolean success) {
                            Log.i(TAG, "taskCompleted: " + success);
                            if (success){
                                activity.finish();
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(activity, "Could not upload Task", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void PushTaskObject(String Abn, final TaskObject taskObject, final Complete complete){
        final DatabaseReference pushRef = mDatabase.child(EMPLOYER_FIREBASE).child(Abn).child(TASK_FIREBASE);
        pushRef.keepSynced(true);
        final List<TaskObject> taskObjectList = new ArrayList<>();

        retrieveListOfTasks(Abn, new Response() {
            @Override
            public void retrieveAbnStringList(List<String> abnList) {

            }

            @Override
            public void retrieveAbnObjectList(List<AbnObject> abnObjects) {

            }

            @Override
            public void retrieveTaskList(List<TaskObject> taskObjects) {
                Log.i(TAG, "retrieveTaskList: triggered" + taskObjects.size());
                boolean exists = false;
                if (taskObjects != null && taskObjects.size() > 0){
                    taskObjectList.addAll(taskObjects);

                    for (TaskObject tO : taskObjectList){
                        if (taskObject.getWorkType().equals(tO.getWorkType()) &&
                                taskObject.getRate() == tO.getRate() &&
                                taskObject.getTask().equals(tO.getTask())){
                            exists = true;
                        }
                    }
                }

                if (!exists){
                    taskObject.setDateAddedToDb(getDateTimeString());
                    taskObject.setUserIdOfCreator(auth.getUid());

                    taskObjectList.add(taskObject);

                    pushRef.setValue(taskObjectList).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i(TAG, "onComplete: isSuccessful = " + task.isSuccessful());
                            complete.taskCompleted(task.isSuccessful());
                        }
                    });
                }else {
                    complete.taskCompleted(true);
                }

            }
        });
    }

    public void UpdateListOfPreviouslyUsedAbns(final String currentAbn, final Complete complete){
        final DatabaseReference listRef = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child("recent" + EMPLOYER_FIREBASE);
        listRef.keepSynced(true);
        final boolean[] exists = {false};

        LoadListOfPreviousAbn(new Response() {
            @Override
            public void retrieveAbnStringList(List<String> abnList) {
                if (abnList.size() > 0){
                    for (String abnString : abnList){
                        if (abnString.equals(currentAbn)){
                            exists[0] = true;
                        }
                    }

                    if (!exists[0]){
                        abnList.add(currentAbn);
                    }
                }

                if (abnList.size() == 0){
                    abnList.add(currentAbn);
                }

                listRef.setValue(abnList).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "onComplete: isSuccessful = " + task.isSuccessful());
                        complete.taskCompleted(task.isSuccessful());
                    }
                });
            }

            @Override
            public void retrieveAbnObjectList(List<AbnObject> abnObjects) {

            }

            @Override
            public void retrieveTaskList(List<TaskObject> taskObjects) {

            }
        });

    }

    public void LoadListOfPreviousAbn(final Response delegate){
        DatabaseReference listRef = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child("recent" + EMPLOYER_FIREBASE);
        listRef.keepSynced(true);
        final List<String> abnList = new ArrayList<>();

        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot abn : dataSnapshot.getChildren()){
                    Log.i(TAG, "onDataChange: abn = " + abn);
                    abnList.add((String) abn.getValue());
                }

                delegate.retrieveAbnStringList(abnList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                delegate.retrieveAbnStringList(null);
            }
        });
    }

    public void retrieveListOfEmployers(final List<String> abnStrings, final Response delegate){
        final List<AbnObject> abnObjects = new ArrayList<>();
        DatabaseReference listRef = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child("recent" + EMPLOYER_FIREBASE);
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot current : dataSnapshot.getChildren()){
                    for (String abnString : abnStrings){
                        if (current.getKey().equals(abnString)){
                            abnObjects.add(current.getValue(AbnObject.class));
                        }
                    }
                }
                delegate.retrieveAbnObjectList(abnObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                delegate.retrieveAbnObjectList(null);
            }
        });
    }

    public void newAbnEntry(final String currentAbn, final matchListener delegate){
        DatabaseReference listRef = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child("recent" + EMPLOYER_FIREBASE);
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot current : dataSnapshot.getChildren()){
                        if (current.getKey().equals(currentAbn)){
                            delegate.abnMatch(true);
                            return;
                        }

                }
                delegate.abnMatch(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                delegate.abnMatch(false);
            }
        });
    }

    public void retrieveListOfTasks (String abn, final Response delegate){
        DatabaseReference taskListReference = mDatabase.child(EMPLOYER_FIREBASE).child(abn).child(TASK_FIREBASE);
        taskListReference.keepSynced(true);
        taskListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TaskObject> taskObjects = new ArrayList<>();
                for (DataSnapshot current : dataSnapshot.getChildren()){
                    taskObjects.add(current.getValue(TaskObject.class));
                }

                delegate.retrieveTaskList(taskObjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                delegate.retrieveTaskList(null);
            }
        });

    }

    public static String getDateTimeString(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);

        return sdf.format(cal.getTime());
    }
}

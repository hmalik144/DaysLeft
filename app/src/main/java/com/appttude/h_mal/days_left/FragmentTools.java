package com.appttude.h_mal.days_left;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.AddItems.AddShiftActivity;
import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.fasterxml.aalto.in.ElementScope;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.appttude.h_mal.days_left.FragmentList.convertDate;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.SHIFT_ID;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.USER_FIREBASE;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;
import static com.appttude.h_mal.days_left.Global.FirebaseClass.mDatabase;

public class FragmentTools extends Fragment {

    private static String TAG = "FragmentTools";

    private DatabaseReference reference;
    private List<ShiftObject> shiftObjectArrayList;
    private Button button;

    private FirebaseFunctions mFunctions;

    private static final int STORAGE_PERMISSIONS = 1331;
    private int selection;
    private Button summery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reference = mDatabase.child(USER_FIREBASE).child(auth.getUid()).child(SHIFT_FIREBASE);
        reference.keepSynced(true);
        shiftObjectArrayList = new ArrayList<>();

        mFunctions = FirebaseFunctions.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tools, container, false);

        button = view.findViewById(R.id.compile);
        summery = view.findViewById(R.id.summary_button);

        reference.addListenerForSingleValueEvent(valueEventListener);
//        if (shiftObjectArrayList.size() > 0){
//            Toast.makeText(getContext(), "Button Active", Toast.LENGTH_SHORT).show();
//            button.setOnClickListener(clickListener);
//            summery.setOnClickListener(clickListener);
//        }else {
//            Toast.makeText(getContext(), "List Empty", Toast.LENGTH_SHORT).show();
//        }
        summery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeToExcel().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                            }


                            Log.w(TAG, "addMessage:onFailure", e);
                            Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        // [START_EXCLUDE]
                        String result = task.getResult();
                        Log.i(TAG, "onComplete: " + result);



                    }
                });
            }
        });
        return view;
    }

    private Task<String> writeToExcel() {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", "wtf!!");
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();

                        Log.i(TAG, "then: " + result);

                        return result;
                    }
                });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                shiftObjectArrayList.add(postSnapshot.getValue(ShiftObject.class));
            }

            if (shiftObjectArrayList.size() > 0){
                Toast.makeText(getContext(), "Button Active", Toast.LENGTH_SHORT).show();
                button.setOnClickListener(clickListener);
//                summery.setOnClickListener(clickListener);
            }else {
                Toast.makeText(getContext(), "List Empty", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.compile:
                    selection = 0;
                    break;
                case R.id.summary_button:
                    selection = 1;
                    break;
            }

            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSIONS);
            }else{
                Toast.makeText(getContext(), "" + selection, Toast.LENGTH_SHORT).show();
                executeCompile();
            }
        }
    };

    private void executeCompile(){
        boolean success = false;
        try {
            if (selection == 0){
                saveExcelFile(getActivity(),"compile",shiftObjectArrayList);
            }else if (selection == 1){
                saveExcelSummaryFile(getActivity(),"summary",shiftObjectArrayList);
            }

            success = true;
        }catch (Exception e){
            Log.e(TAG, "onClick: ",e );
        }finally {
            if (success){
                //open the file
                Log.i(TAG, "executeCompile: Success");
            }
        }
    }

    private static boolean saveExcelFile(Activity activity, String fileName, List<ShiftObject> shifts) {

        String[] strings = {"Employer name","ABN","Shift Date","Time in","Time out","Break","Hours","Units","Pay","Work Type"};

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Shift List");

        // Generate column headings
        Row row = sheet1.createRow(0);

        //Initialise top row
        for (int i=0; i<strings.length; i++){
            c = row.createCell(i);
            c.setCellValue(strings[i]);
        }

        sortArrayByDate(shifts);

        int r = 1;
        for (ShiftObject shift : shifts){
            row = sheet1.createRow(r);
            for (int i=0; i<strings.length; i++){
                c = row.createCell(i);

                switch (i) {
                    case 0:
                        c.setCellValue(shift.getAbnObject().getCompanyName());
                        break;
                    case 1:
                        c.setCellValue(shift.getAbnObject().getAbn());
                        break;
                    case 2:
                        c.setCellValue(shift.getShiftDate());
                        break;
                    case 3:
                        if (shift.timeObject != null){
                            c.setCellValue(shift.getTimeObject().getTimeIn());
                        }
                        break;
                    case 4:
                        if (shift.timeObject != null) {
                            c.setCellValue(shift.getTimeObject().getTimeOut());
                        }
                        break;
                    case 5:
                        if (shift.timeObject != null) {
                            c.setCellValue(shift.getTimeObject().getBreakEpoch());
                        }
                        break;
                    case 6:
                        if (shift.timeObject != null) {
                            c.setCellValue(shift.getTimeObject().getHours());
                        }
                        break;
                    case 7:
                        if (shift.unitsCount != null){
                            c.setCellValue(shift.getUnitsCount());
                        }
                        break;
                    case 8:
                        float units;
                        if (shift.unitsCount != null){
                            units = shift.getUnitsCount();
                        }else{
                            units = shift.getTimeObject().getHours();
                        }
                        c.setCellValue(shift.getTaskObject().getRate() * units);
                        break;
                    case 9:
                        c.setCellValue(shift.getTaskObject().getWorkType());
                        break;
                }
            }
            r++;
        }

        sheet1.setColumnWidth(0, (15 * 500));

        try {
            createAndOpenFile(fileName,wb,activity);
            success = true;
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
        }

        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSIONS){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    executeCompile();
            }else{
                Toast.makeText(getContext(), "No Storage Permissions", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private static boolean saveExcelSummaryFile(Activity activity, String fileName, List<ShiftObject> shifts) {
        List<String> abnList = new ArrayList<>();
        String[] strings = {"ABN","Post code","Start Date","End date"};

        //add abn numbers to list
        for(ShiftObject currentShift : shifts){
            String currentAbn = currentShift.getAbnObject().getAbn();

            if (!abnList.contains(currentAbn)) {
                abnList.add(currentAbn);
                Log.i(TAG, "saveExcelSummaryFile: current abn = " + currentAbn);
            }


        }

        //sort by date
        sortArrayByDate(shifts);

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Visa summary List");

        // Generate column headings
        Row row = sheet1.createRow(0);

        //Initialise top row
        for (int i=0; i<strings.length; i++){
            c = row.createCell(i);
            c.setCellValue(strings[i]);
        }

        int r = 1;


        //create shifts for export arraylist
        List<ShiftObject> tempArray = new ArrayList<>();

        //loop through abnList and add to excel
        for(String abn : abnList){
            //add shifts to tempArray
            for(ShiftObject tempShift : shifts){
                if (tempShift.getAbnObject().getAbn().equals(abn)){
                    //exists
                    tempArray.add(tempShift);
                    Log.i(TAG, "saveExcelSummaryFile: " + tempShift.getAbnObject().getAbn());
                }
            }

            row = sheet1.createRow(r);
            for (int i=0; i<strings.length; i++){
                c = row.createCell(i);

                switch (i) {
                    case 0:
                        c.setCellValue(tempArray.get(0).getAbnObject().getAbn());
                        break;
                    case 1:
                        c.setCellValue(tempArray.get(0).getAbnObject().getPostCode());
                        break;
                    case 2:
                        c.setCellValue(tempArray.get(0).getShiftDate());
                        break;
                    case 3:
                        c.setCellValue(tempArray.get(tempArray.size() - 1).getShiftDate());
                        break;
                }

            }

            r++;
            tempArray.clear();
        }

        sheet1.setColumnWidth(0, (6 * 500));
        sheet1.setColumnWidth(1, (5 * 500));
        sheet1.setColumnWidth(2, (6 * 500));
        sheet1.setColumnWidth(3, (6 * 500));

        // Create a path where we will place our List of objects on external storage
        try {
            createAndOpenFile(fileName,wb,activity);
            success = true;
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
        }

        return success;

    }

    private static void sortArrayByDate(List<ShiftObject> shifts){
        Collections.sort(shifts, new Comparator<ShiftObject>() {

            public int compare(ShiftObject s1, ShiftObject s2) {
                Date d1 = convertDate(s1.getShiftDate());
                Date d2 = convertDate(s2.getShiftDate());

                return d1.compareTo(d2);
            }});
    }

    private static void createAndOpenFile(String fileName,Workbook wb, Activity activity){
        final String savePath = Environment.getExternalStorageDirectory() + "/ShifttrackerTemp";
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File myFile = new File(savePath,fileName + ".xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(myFile);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
                Log.i(TAG, "saveExcelFile: filepath = " + myFile.getAbsolutePath());
            }
        }

        final Uri data = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", myFile);
        activity.grantUriPermission(activity.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        final Intent intent1 = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "application/vnd.ms-excel")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            activity.startActivity(intent1);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
        }
    }
}

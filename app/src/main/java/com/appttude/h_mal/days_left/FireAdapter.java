package com.appttude.h_mal.days_left;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.Objects.TimeObject;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


class FireAdapter extends FirebaseListAdapter<ShiftObject> {

    String TAG = "FireAdapter";

    List<ShiftObject> shiftObjects;

    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into
     *                    an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item.
     *                    You will be responsible for populating an instance of the corresponding
     *                    view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location,
     *                    using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public FireAdapter(Activity activity, Class<ShiftObject> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        shiftObjects = new ArrayList<>();
    }

    @Override
    protected void populateView(View listItemView, ShiftObject currentShift, final int position) {

        TextView farmNameTextView = listItemView.findViewById(R.id.farm_name);
        TextView dateTextView = listItemView.findViewById(R.id.date);
        TextView tastTextView = listItemView.findViewById(R.id.task_name);
        TextView typeTextView = listItemView.findViewById(R.id.type);
        LinearLayout timeHolderLinearLayout = listItemView.findViewById(R.id.time_holder);
        TextView timeTextView = listItemView.findViewById(R.id.time);
        LinearLayout breakHolderLinearLayout = listItemView.findViewById(R.id.break_holder);
        TextView breakTimeTextView = listItemView.findViewById(R.id.break_time);
        LinearLayout unitsHolderLinearLayout = listItemView.findViewById(R.id.units_holder);
        TextView unitsTextView = listItemView.findViewById(R.id.units);
        TextView locationTextView = listItemView.findViewById(R.id.location);

        dateTextView.setText(currentShift.getShiftDate());

        TaskObject taskObject = currentShift.getTaskObject();
        tastTextView.setText(taskObject.getTask());
        String s = taskObject.getWorkType() + " - $" + taskObject.getRate() + "/";
        if (taskObject.getWorkType().equals("Hourly")){
            s = s + "Hour";
            timeHolderLinearLayout.setVisibility(View.VISIBLE);
            unitsHolderLinearLayout.setVisibility(View.GONE);

            TimeObject timeObject = currentShift.getTimeObject();

            timeTextView.setText(timeObject.getTimeIn() + " - " + timeObject.getTimeOut());

            if (timeObject.getBreakEpoch() > 0){
                breakHolderLinearLayout.setVisibility(View.VISIBLE);
                String breakString = getBreakTimeString(timeObject.getBreakEpoch());
                breakTimeTextView.setText(breakString);
            }else {
                breakHolderLinearLayout.setVisibility(View.GONE);
            }
            unitsTextView.setText(String.valueOf(timeObject.getHours()));

        }else {
            s = s + "Unit";
            timeHolderLinearLayout.setVisibility(View.GONE);
            unitsHolderLinearLayout.setVisibility(View.VISIBLE);

            unitsTextView.setText(String.valueOf(currentShift.getUnitsCount()));
        }

        typeTextView.setText(s);
        farmNameTextView.setText(currentShift.getAbnObject().getCompanyName());

        locationTextView.setText(currentShift.getAbnObject().getState() + " - " + currentShift.getAbnObject().getPostCode());

    }

    public String getId(int i){
        return getRef(i).getKey();
    }

    @Override
    protected ShiftObject parseSnapshot(DataSnapshot snapshot) {
        shiftObjects.add(snapshot.getValue(ShiftObject.class));
        return super.parseSnapshot(snapshot);
    }



    private String getBreakTimeString(int breakMins){
        float hoursFloat = breakMins/60;

        int hoursInt = (int) Math.floor(hoursFloat);
        int minsInt = breakMins - (hoursInt*60);

        String s = "";
        if (hoursInt > 0){
            s = hoursInt + " h" + " ";
        }

        if (minsInt > 0){
            s = s + minsInt + " m";
        }

        return s;
    }

}

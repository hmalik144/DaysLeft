package com.appttude.h_mal.days_left;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appttude.h_mal.days_left.Objects.ShiftObject;
import com.appttude.h_mal.days_left.Objects.TaskObject;
import com.appttude.h_mal.days_left.Objects.TimeObject;

import java.util.ArrayList;
import java.util.Map;

public class MapListViewAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private final ArrayList mData;

    public MapListViewAdapter(Context context, Map<String,ShiftObject> map) {
        this.context = context;
        mData = new ArrayList();
        mData.addAll(map.entrySet());

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public ShiftObject getItem(int position) {
        return (ShiftObject)((Map.Entry) mData.get(position)).getValue();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getId(int position){
        return (String) ((Map.Entry) mData.get(position)).getKey();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (convertView == null){
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
        }

        ShiftObject currentShift = getItem(position);

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

        return listItemView;
    }

    @Override
    public Filter getFilter() {
        return null;
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

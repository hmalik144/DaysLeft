package com.appttude.h_mal.days_left.Objects;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.appttude.h_mal.days_left.Abn.AbnObject;
import com.appttude.h_mal.days_left.Login.LoginFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ShiftObject {

    public String shiftDate;
    public String dateTimeAdded;
    public AbnObject abnObject;
    public TaskObject taskObject;
    public Float unitsCount;
    public TimeObject timeObject;

    public ShiftObject(String shiftDate, String dateTimeAdded, AbnObject abnObject, TaskObject taskObject, @Nullable Float unitsCount, @Nullable TimeObject timeObject) {
        this.shiftDate = shiftDate;
        this.dateTimeAdded = dateTimeAdded;
        this.abnObject = abnObject;
        this.taskObject = taskObject;
        this.unitsCount = unitsCount;
        this.timeObject = timeObject;
    }

    public ShiftObject() {
    }

    public String getShiftDate() {
        return shiftDate;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public AbnObject getAbnObject() {
        return abnObject;
    }

    public TaskObject getTaskObject() {
        return taskObject;
    }

    public Float getUnitsCount() {
        return unitsCount;
    }

    public TimeObject getTimeObject() {
        return timeObject;
    }

    public void setShiftDate(String shiftDate) {
        this.shiftDate = shiftDate;
    }

    public void changeDate(){
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = dt.parse(dateTimeAdded);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.i("DateChange", "changeDate: " + dateTimeAdded + " -> " + dt1.format(date));

        this.dateTimeAdded = dt1.format(date);
    }

}

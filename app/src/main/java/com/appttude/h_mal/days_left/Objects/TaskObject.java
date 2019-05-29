package com.appttude.h_mal.days_left.Objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class TaskObject implements Parcelable {

    private String workType;
    private float rate;
    private String task;
    private String dateAddedToDb;
    private String userIdOfCreator;

    public TaskObject() {
    }

    public TaskObject(String workType, float rate, String task, @Nullable String dateAddedToDb, @Nullable String userIdOfCreater) {
        this.workType = workType;
        this.rate = rate;
        this.task = task;
        this.dateAddedToDb = dateAddedToDb;
        this.userIdOfCreator = userIdOfCreater;
    }

    protected TaskObject(Parcel in) {
        workType = in.readString();
        rate = in.readFloat();
        task = in.readString();
        dateAddedToDb = in.readString();
        userIdOfCreator = in.readString();
    }

    public static final Creator<TaskObject> CREATOR = new Creator<TaskObject>() {
        @Override
        public TaskObject createFromParcel(Parcel in) {
            return new TaskObject(in);
        }

        @Override
        public TaskObject[] newArray(int size) {
            return new TaskObject[size];
        }
    };

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDateAddedToDb() {
        return dateAddedToDb;
    }

    public void setDateAddedToDb(String dateAddedToDb) {
        this.dateAddedToDb = dateAddedToDb;
    }

    public String getUserIdOfCreator() {
        return userIdOfCreator;
    }

    public void setUserIdOfCreator(String userIdOfCreator) {
        this.userIdOfCreator = userIdOfCreator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(workType);
        dest.writeFloat(rate);
        dest.writeString(task);
        dest.writeString(dateAddedToDb);
        dest.writeString(userIdOfCreator);
    }
}

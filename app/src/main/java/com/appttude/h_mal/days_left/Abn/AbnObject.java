package com.appttude.h_mal.days_left.Abn;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class AbnObject implements Parcelable {

    private String abn;
    private String companyName;
    private int postCode;
    private String state;
    private String dateAdded;
    private String addedById;
    private Boolean fromAbnSearch;

    public AbnObject(String abn, String companyName, int postCode, String state, @Nullable String dateAdded, @Nullable String addedById, Boolean fromAbnSearch) {
        this.abn = abn;
        this.companyName = companyName;
        this.postCode = postCode;
        this.state = state;
        this.dateAdded = dateAdded;
        this.addedById = addedById;
        this.fromAbnSearch = fromAbnSearch;
    }

    public AbnObject() {
    }

    protected AbnObject(Parcel in) {
        abn = in.readString();
        companyName = in.readString();
        postCode = in.readInt();
        state = in.readString();
        dateAdded = in.readString();
        addedById = in.readString();
        byte tmpFromAbnSearch = in.readByte();
        fromAbnSearch = tmpFromAbnSearch == 0 ? null : tmpFromAbnSearch == 1;
    }

    public static final Creator<AbnObject> CREATOR = new Creator<AbnObject>() {
        @Override
        public AbnObject createFromParcel(Parcel in) {
            return new AbnObject(in);
        }

        @Override
        public AbnObject[] newArray(int size) {
            return new AbnObject[size];
        }
    };

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getAddedById() {
        return addedById;
    }

    public void setAddedById(String addedById) {
        this.addedById = addedById;
    }

    public Boolean getFromAbnSearch() {
        return fromAbnSearch;
    }

    public void setFromAbnSearch(Boolean fromAbnSearch) {
        this.fromAbnSearch = fromAbnSearch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(abn);
        dest.writeString(companyName);
        dest.writeInt(postCode);
        dest.writeString(state);
        dest.writeString(dateAdded);
        dest.writeString(addedById);
        dest.writeByte((byte) (fromAbnSearch == null ? 0 : fromAbnSearch ? 1 : 2));
    }
}

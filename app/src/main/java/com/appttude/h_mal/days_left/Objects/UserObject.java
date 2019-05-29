package com.appttude.h_mal.days_left.Objects;

public class UserObject {

    public String profileName;
    public String profileEmail;
    public String profilePicString;

    public UserObject() {
    }

    public UserObject(String profileName, String profileEmail, String profilePicString) {
        this.profileName = profileName;
        this.profileEmail = profileEmail;
        this.profilePicString = profilePicString;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public String getProfilePicString() {
        return profilePicString;
    }
}

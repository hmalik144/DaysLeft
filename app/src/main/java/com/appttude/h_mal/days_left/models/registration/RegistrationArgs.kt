package com.appttude.h_mal.days_left.models.registration

import android.os.Parcel
import android.os.Parcelable

data class RegistrationArgs(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegistrationArgs> {
        override fun createFromParcel(parcel: Parcel): RegistrationArgs {
            return RegistrationArgs(parcel)
        }

        override fun newArray(size: Int): Array<RegistrationArgs?> {
            return arrayOfNulls(size)
        }
    }

}
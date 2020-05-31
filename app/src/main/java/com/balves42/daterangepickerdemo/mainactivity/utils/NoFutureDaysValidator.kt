package com.balves42.daterangepickerdemo.mainactivity.utils

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints

class NoFutureDaysValidator() : CalendarConstraints.DateValidator {
    constructor(parcel: Parcel) : this()

    override fun writeToParcel(dest: Parcel?, flags: Int) {}

    override fun isValid(date: Long): Boolean {
        val currentDate = System.currentTimeMillis()
        return date < currentDate
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NoFutureDaysValidator> {
        override fun createFromParcel(parcel: Parcel): NoFutureDaysValidator {
            return NoFutureDaysValidator(
                parcel
            )
        }

        override fun newArray(size: Int): Array<NoFutureDaysValidator?> {
            return arrayOfNulls(size)
        }
    }
}
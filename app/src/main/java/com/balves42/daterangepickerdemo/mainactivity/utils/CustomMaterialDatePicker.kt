package com.balves42.daterangepickerdemo.mainactivity.utils

import android.content.DialogInterface
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.balves42.daterangepickerdemo.R
import com.balves42.daterangepickerdemo.mainactivity.MainActivityView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlin.math.abs

class CustomMaterialDatePicker(
    private val mView: MainActivityView,
    private val mMaxDateRange: Long? = null,
    private val mTitleText: String? = null,
    private val mButtonText: String? = null,
    private val mHeaderText: Pair<String, String>? = null,
    private val mIsoCode: String? = null
) {

    private var mMaterialDatePicker: MaterialDatePicker<Pair<Long, Long>>? = null
    private var mMaterialPositiveListener: MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>? =
        null
    private var mMaterialOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mMaterialOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mCalendarConfirmView: View? = null
    private var mCalendarEnabled = true

    init {
        build()
    }

    fun show(
        fragmentManager: FragmentManager?
    ) {
        try {
            if (mCalendarEnabled) {
                setCalendarEnabled(false)
                mMaterialDatePicker?.show(
                    fragmentManager!!,
                    mMaterialDatePicker.toString()
                )
                addSelectionObserver()
            }
        } catch (e: IllegalStateException) {
            setCalendarEnabled(true)
            removeListeners()
            build()
        }

    }

    fun removeListeners() {
        removeDateRangeListeners()
        removeOnDismissListener()
        removeOnCancelListener()
        mMaterialDatePicker?.selectionLive?.removeObservers(mView as LifecycleOwner)
    }

    private fun enableConfirmView(enable: Boolean) {
        mCalendarConfirmView?.isEnabled = enable
    }

    private fun build() {
        setCalendarEnabled(true)
        initDateRangeListeners()
        initOnDismissListener()
        initOnCancelListener()
        createDateRangePicker()
        addDateRangeListeners()
        addOnDismissListeners()
        addOnCancelListeners()
    }

    private fun createDateRangePicker() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(NoFutureDaysValidator())
        constraintsBuilder.setEnd(System.currentTimeMillis()) //set current month as max
        builder.setCalendarConstraints(constraintsBuilder.build())

        mTitleText?.let {
            builder.setTitleText(it)
        }
        mHeaderText?.let {
            builder.setHeaderText(it.first, it.second)
        }
        mButtonText?.let {
            builder.setConfirmButtonText(it)
        }
        mIsoCode?.let {
            builder.setLocale(it)
        }
        mMaterialDatePicker = builder.build()
    }

    private fun initDateRangeListeners() {
        mMaterialPositiveListener = MaterialPickerOnPositiveButtonClickListener { values ->
            setCalendarEnabled(true)
            values?.first?.let { start ->
                values.second?.let { end ->
                    mView.selectedBehavior(Pair(start, end))
                }
            }
        }
    }

    private fun exitBehaviour() {
        setCalendarEnabled(true)
    }

    private fun initOnDismissListener() {
        mMaterialOnDismissListener = DialogInterface.OnDismissListener {
            exitBehaviour()
        }
    }

    private fun initOnCancelListener() {
        mMaterialOnCancelListener = DialogInterface.OnCancelListener {
            exitBehaviour()
        }
    }

    private fun addSelectionObserver() {
        mMaterialDatePicker?.let {
            if (!it.selectionLive.hasActiveObservers()) {
                it.selectionLive.observe(mView as LifecycleOwner, Observer { pair ->
                    val start = pair.first!!
                    val end = pair.second!!
                    mMaxDateRange?.let { maxDateRange ->
                        setCalendarConfirmView()
                        if (abs(end - start) > maxDateRange) {
                            enableConfirmView(false)
                            mView.maxDateRangeBehaviour()
                        }
                    }
                }
                )
            }
        }
    }

    private fun setCalendarConfirmView() {
        mCalendarConfirmView = mMaterialDatePicker?.dialog?.findViewById(R.id.confirm_button)
    }

    private fun addDateRangeListeners() {
        mMaterialDatePicker?.addOnPositiveButtonClickListener(mMaterialPositiveListener)
    }

    private fun addOnDismissListeners() {
        mMaterialDatePicker?.addOnDismissListener(mMaterialOnDismissListener)
    }

    private fun addOnCancelListeners() {
        mMaterialDatePicker?.addOnCancelListener(mMaterialOnCancelListener)
    }

    private fun removeDateRangeListeners() {
        mMaterialDatePicker?.removeOnPositiveButtonClickListener(mMaterialPositiveListener)
    }

    private fun removeOnDismissListener() {
        mMaterialDatePicker?.removeOnDismissListener(mMaterialOnDismissListener)
    }

    private fun removeOnCancelListener() {
        mMaterialDatePicker?.removeOnCancelListener(mMaterialOnCancelListener)
    }

    private fun setCalendarEnabled(enable: Boolean) {
        mCalendarEnabled = enable
    }
}
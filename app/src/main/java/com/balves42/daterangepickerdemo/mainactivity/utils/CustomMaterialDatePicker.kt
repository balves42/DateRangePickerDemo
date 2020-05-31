package com.balves42.daterangepickerdemo.mainactivity.utils

import android.content.DialogInterface
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageButton
import androidx.core.util.Pair
import androidx.fragment.app.FragmentManager
import com.balves42.daterangepickerdemo.R
import com.balves42.daterangepickerdemo.mainactivity.MainActivityView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlin.math.abs

class CustomMaterialDatePicker(
    private val mView: MainActivityView,
    private val mOtherView: View,
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
    private var mCalendarListener: View.OnLayoutChangeListener? = null
    private var mOtherViewListener: ViewTreeObserver.OnWindowFocusChangeListener? = null
    private var mCalendarConfirmView: View? = null
    private var mCalendarView: View? = null
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
        removeCalendarListener()
        removeOtherViewListener()
    }

    private fun enableConfirmView(enable: Boolean) {
        mCalendarConfirmView?.isEnabled = enable
    }

    private fun build() {
        setCalendarEnabled(true)
        initCalendarListener()
        initOtherViewListener()
        addOtherViewListener()
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
            removeCalendarListener()
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

    private fun initOtherViewListener() {
        mOtherViewListener = ViewTreeObserver.OnWindowFocusChangeListener {
            mCalendarView = mMaterialDatePicker?.dialog?.findViewById(R.id.mtrl_calendar_frame)
            mCalendarConfirmView = mMaterialDatePicker?.dialog?.findViewById(R.id.confirm_button)
            if (mCalendarView != null && mCalendarConfirmView != null) {
                val inputChangeButton =
                    mMaterialDatePicker?.dialog?.findViewById<ImageButton>(R.id.mtrl_picker_header_toggle)
                inputChangeButton?.visibility = View.INVISIBLE
                removeCalendarListener()
                addCalendarListener()
            }
        }
    }

    private fun initCalendarListener() {
        mCalendarListener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            mMaxDateRange?.let { maxDateRange ->
                mMaterialDatePicker?.selection?.first?.let { start ->
                    mMaterialDatePicker?.selection?.second?.let { end ->
                        if (abs(end - start) > maxDateRange) {
                            enableConfirmView(false)
                            mView.maxDateRangeBehaviour()
                        }
                    }
                }
            }
        }
    }

    private fun addCalendarListener() {
        mCalendarView?.addOnLayoutChangeListener(mCalendarListener)
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

    private fun addOtherViewListener() {
        mOtherView.viewTreeObserver.addOnWindowFocusChangeListener(mOtherViewListener)
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

    private fun removeCalendarListener() {
        mCalendarView?.removeOnLayoutChangeListener(mCalendarListener)
    }

    private fun removeOtherViewListener() {
        mOtherView.viewTreeObserver.removeOnWindowFocusChangeListener(mOtherViewListener)
    }

    private fun setCalendarEnabled(enable: Boolean) {
        mCalendarEnabled = enable
    }

}
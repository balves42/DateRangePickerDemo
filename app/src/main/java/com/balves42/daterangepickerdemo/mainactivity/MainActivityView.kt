package com.balves42.daterangepickerdemo.mainactivity

import androidx.core.util.Pair

interface MainActivityView {

    /**
     * Behaviour when are selected more than the max days
     *
     */
    fun maxDateRangeBehaviour()

    /**
     * Behaviour when a correct number of days are selected
     *
     * @param range Pair with start and end date in milliseconds
     */
    fun selectedBehavior(range: Pair<Long, Long>?)
}
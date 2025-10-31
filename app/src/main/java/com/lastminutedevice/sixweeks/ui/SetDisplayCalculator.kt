package com.lastminutedevice.sixweeks.ui

import android.content.res.Resources
import com.lastminutedevice.sixweeks.R
import java.util.Locale

class SetDisplayCalculator(private val resources: Resources) {

    /**
     * Formats reps for display. Many programs just display the number of reps.
     * The plank program displays minutes and seconds.
     */
    fun repString(reps: Int) : String {
        return if (resources.getBoolean(R.bool.display_minutes)) {
            val minutes = reps / 60
            val seconds = String.format(Locale.US, "%02d", reps.mod(60))
            return "$minutes:$seconds"
        } else {
            reps.toString()
        }
    }
}

package com.lastminutedevice.sixweeks.ui.program

import androidx.recyclerview.widget.DiffUtil

/**
 * A workout's completion status will change as the user progresses through the workouts.
 * This DiffUtil checks that boolean to determine if the list item needs to be redrawn.
 */
class UserWorkoutDiffUtil : DiffUtil.ItemCallback<ProgramAdapterItem>() {

    override fun areItemsTheSame(oldItem: ProgramAdapterItem, newItem: ProgramAdapterItem): Boolean {
        return oldItem.week == newItem.week && oldItem.workout?.id == newItem.workout?.id
    }

    override fun areContentsTheSame(oldItem: ProgramAdapterItem, newItem: ProgramAdapterItem): Boolean {
        return oldItem.workout?.completed == newItem.workout?.completed
    }
}

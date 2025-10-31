package com.lastminutedevice.sixweeks.ui.program

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.R
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterItemDayBinding
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterWeekHeaderBinding
import com.lastminutedevice.sixweeks.ui.SetDisplayCalculator

class ProgramAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listDiffer = AsyncListDiffer(this, UserWorkoutDiffUtil())

    override fun getItemCount(): Int = listDiffer.currentList.size

    override fun getItemViewType(position: Int): Int {
        return if (listDiffer.currentList[position].workout == null) {
            ProgramAdapterItemType.HEADER.ordinal
        } else {
            ProgramAdapterItemType.ITEM.ordinal
        }
    }

    fun updateList(newList: List<UserWorkout>) {
        // If there's only one "week", don't show the header.
        // Otherwise create a header for each distinct week.
        val weeks = newList.map { it.week }.distinct()
        val headers = if (weeks.size > 1) {
            weeks.map { ProgramAdapterItem(week = it) }
        } else {
            emptyList()
        }

        // Crate an item for each workout.
        val workouts = newList.map { item ->
            ProgramAdapterItem(week = item.week, workout = item)
        }

        // Sorted list of headers and items.
        listDiffer.submitList((headers + workouts).sorted())
    }

    fun firstIncompletePosition(): Int {
        return listDiffer.currentList.indexOfFirst { item ->
            item.workout?.completed == null
        } + 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ProgramAdapterItemType.ITEM.ordinal -> {
                return ProgramItemHolder(
                    binding = ProgramAdapterItemDayBinding.inflate(inflater, parent, false)
                )
            }

            else -> ProgramHeaderHolder(
                binding = ProgramAdapterWeekHeaderBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = listDiffer.currentList[position]
        if (holder is ProgramHeaderHolder) {
            holder.bindWeek(week = item.week)
        } else if (holder is ProgramItemHolder && item.workout != null) {
            holder.bindWorkout(workout = item.workout)
        } else {
            Log.e("ProgramAdapter", "Error: no workout in this list item: $position.")
        }
    }

    private fun ProgramHeaderHolder.bindWeek(week: Int) {
        weekView.text = itemView.resources.getString(R.string.program_list_header, week)
    }

    private fun ProgramItemHolder.bindWorkout(workout: UserWorkout) {
        // Which day of the workout.
        dayView.text = itemView.resources.getString(R.string.program_list_day, workout.day)
        // What is in the workout.
        if (workout.sets.isEmpty()) {
            setsView.text = itemView.resources.getString(R.string.program_rest_day)
        } else {
            setsView.text = workout.sets.joinToString(", ") { reps ->
                SetDisplayCalculator(itemView.resources).repString(reps)
            }
        }
        // Finished or not.
        if (workout.completed != null) {
            completedView.visibility = View.VISIBLE
            notCompletedView.visibility = View.GONE
        } else {
            completedView.visibility = View.GONE
            notCompletedView.visibility = View.VISIBLE
        }
    }
}

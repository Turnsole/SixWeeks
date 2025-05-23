package com.lastminutedevice.sixweeks.ui.program

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.R
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterItemDayBinding
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterWeekBinding

class ProgramAdapter() : RecyclerView.Adapter<ProgramViewHolder>() {

    val weekList : MutableList<Int> = mutableListOf()

    var workoutMap : Map<Int, List<UserWorkout>> = mapOf()

    fun updateList(newList: Map<Int, List<UserWorkout>>) {
        /* todo - DiffUtils
         * The workouts don't change, only completion status. Find the item
         * whose completion status changed, and update that item.
         */
        weekList.clear()
        weekList.addAll(newList.keys.sortedBy { it })
        workoutMap = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgramViewHolder {
        return ProgramViewHolder(
            binding = ProgramAdapterWeekBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(
        holder: ProgramViewHolder,
        position: Int
    ) {
        val week : Int = weekList[position]
        val workouts : List<UserWorkout> = workoutMap[week]!!
        holder.week.apply {
            text = context.getString(R.string.program_list_header, week)
        }

        holder.container.removeAllViews()
        workouts.forEach { workout ->
            val setView = ProgramAdapterItemDayBinding.inflate(LayoutInflater.from(holder.itemView.context))
            setView.day.text = holder.container.context.getString(
                R.string.program_list_day,
                workout.day,
                workout.sets.joinToString(", ")
            )
            holder.container.addView(setView.root)
        }
    }

    override fun getItemCount(): Int = weekList.size
}

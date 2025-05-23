package com.lastminutedevice.sixweeks.ui.program

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.data.models.UserWorkout
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterItemBinding

class ProgramAdapter(val list: List<UserWorkout>) : RecyclerView.Adapter<ProgramViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProgramViewHolder {
        return ProgramViewHolder(
            binding = ProgramAdapterItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(
        holder: ProgramViewHolder,
        position: Int
    ) {
        val workout = list[position]

        holder.week.text = workout.week.toString()

        holder.day.text = workout.day.toString()

        holder.rest.text = workout.rest.toString()
    }

    override fun getItemCount(): Int = list.size
}

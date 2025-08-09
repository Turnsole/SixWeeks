package com.lastminutedevice.sixweeks.ui.program

import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterItemDayBinding

class ProgramItemHolder(binding: ProgramAdapterItemDayBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val dayView = binding.day
    val setsView = binding.sets
    val completedView = binding.completed
    val notCompletedView = binding.notCompleted
}

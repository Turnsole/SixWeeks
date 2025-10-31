package com.lastminutedevice.sixweeks.ui.program

import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterWeekHeaderBinding

class ProgramHeaderHolder(binding: ProgramAdapterWeekHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val weekView = binding.week
}

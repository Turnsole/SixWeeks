package com.lastminutedevice.sixweeks.ui.program

import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterWeekBinding

class ProgramViewHolder (binding: ProgramAdapterWeekBinding) : RecyclerView.ViewHolder(binding.root) {

    val week = binding.week

    val container = binding.container
}

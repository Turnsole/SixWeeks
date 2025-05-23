package com.lastminutedevice.sixweeks.ui.program

import androidx.recyclerview.widget.RecyclerView
import com.lastminutedevice.sixweeks.databinding.ProgramAdapterItemBinding

class ProgramViewHolder (binding: ProgramAdapterItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val week = binding.week

    val day = binding.day

    val rest = binding.rest
}

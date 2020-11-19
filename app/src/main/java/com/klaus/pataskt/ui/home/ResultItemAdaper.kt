package com.klaus.pataskt.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klaus.pataskt.R

class ResultItemAdaper(val context: Context, private val records: ArrayList<String>): RecyclerView.Adapter<ResultItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false)
        return ResultItemHolder(view)
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: ResultItemHolder, position: Int) {
        holder.bind(records.get(position))
    }
}
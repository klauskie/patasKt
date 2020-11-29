package com.klaus.pataskt.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.klaus.pataskt.R
import com.klaus.pataskt.data.model.RecordModel

class ResultItemAdaper(val context: Context, var records: ArrayList<RecordModel>?): RecyclerView.Adapter<ResultItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultItemHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false)
        return ResultItemHolder(view)
    }

    override fun getItemCount(): Int {
        return records?.size ?: 0
    }

    override fun onBindViewHolder(holder: ResultItemHolder, position: Int) {
        holder.bind(records?.get(position) ?: return)
    }
}
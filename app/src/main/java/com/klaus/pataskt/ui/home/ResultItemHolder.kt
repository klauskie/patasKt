package com.klaus.pataskt.ui.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ResultItemHolder(v: View): RecyclerView.ViewHolder(v) {
    private var view: View = v
    private var _record: String? = null

    fun bind(record: String) {
        _record = record
    }

    companion object {
        private val RECORD_KEY = "RECORD"
    }
}
package com.klaus.pataskt.ui.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.klaus.pataskt.R
import com.klaus.pataskt.data.model.RecordModel
import com.klaus.pataskt.ui.scanner.ResultFragment
import kotlinx.android.synthetic.main.fragment_photo_result.*

class ResultItemHolder(v: View): RecyclerView.ViewHolder(v) {
    private var view: View = v

    fun bind(record: RecordModel) {
        val status = view.findViewById<TextView>(R.id.status_edit)
        val date = view.findViewById<TextView>(R.id.date_edit)
        val imageBanner = view.findViewById<ImageView>(R.id.image_result)

        status.text = record.categoryLabel
        date.text = record.date
        when (record.category) {
            "1" -> {
                imageBanner.setImageResource(R.drawable.result_image_initial)
            }
            "2" -> {
                imageBanner.setImageResource(R.drawable.result_image_moderate)
            }
            "3" -> {
                imageBanner.setImageResource(R.drawable.result_image_advanced)
            }
        }
    }

    companion object {
        private val RECORD_KEY = "RECORD"
    }
}
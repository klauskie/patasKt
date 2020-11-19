package com.klaus.pataskt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.klaus.pataskt.R

class HomeFragment : Fragment() {

    private var TAG = HomeFragment::class.java.simpleName

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val instructionBox = view.findViewById<LinearLayout>(R.id.instructions_box)

        val resultRecyclerView = view.findViewById<RecyclerView>(R.id.result_recycler_view)
        resultRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resultRecyclerView.adapter = ResultItemAdaper(context!!, getRecords())

        if (getRecords().size > 0) {
            instructionBox.visibility = View.INVISIBLE
        }

        return view
    }

    fun getRecords(): ArrayList<String> {
        return ArrayList<String>().apply {
            add("One")
            add("Two")
            add("Three")
            add("Four")
        }
    }

}
package com.klaus.pataskt.ui.home

import android.content.Context
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
import com.klaus.pataskt.util.Constant

class HomeFragment : Fragment() {

    private var TAG = HomeFragment::class.java.simpleName

    private lateinit var homeViewModel: HomeViewModel
    lateinit var recordList: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val instructionBox = view.findViewById<LinearLayout>(R.id.instructions_box)

        val resultRecyclerView = view.findViewById<RecyclerView>(R.id.result_recycler_view)
        resultRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val sharedPref = activity?.getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val code = sharedPref?.getString(Constant.KEY_MED_CODE, "") ?: ""

        if (!code.isEmpty()) {
            recordList = getRecords()
            resultRecyclerView.adapter = ResultItemAdaper(context!!, getRecords())
        }


        if (this::recordList.isInitialized) {
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
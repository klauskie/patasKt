package com.klaus.pataskt.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.klaus.pataskt.R
import com.klaus.pataskt.data.model.RecordModel
import com.klaus.pataskt.service.ApiFetcher
import com.klaus.pataskt.util.Constant
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var resultRecyclerView: RecyclerView
    lateinit var instructionBox: LinearLayout
    lateinit var recordList: ArrayList<RecordModel>
    lateinit var homeListener: IHomeFragment

    interface IHomeFragment {
        fun getHistory(): ArrayList<RecordModel>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        instructionBox = view.findViewById(R.id.instructions_box)

        resultRecyclerView = view.findViewById(R.id.result_recycler_view)
        resultRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val sharedPref = activity?.getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val code = sharedPref?.getString(Constant.KEY_MED_CODE, "") ?: ""


        recordList = homeListener.getHistory()
        if (!code.isEmpty() && !recordList.isEmpty()) {
            resultRecyclerView.adapter = ResultItemAdaper(context!!, recordList)
            instructionBox.visibility = View.INVISIBLE
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IHomeFragment) {
            homeListener = context
        } else {
            Log.d(TAG, "Activity must implement the IHomeFragment interface")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG, "DEEEED")
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}
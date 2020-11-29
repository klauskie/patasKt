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
    var recordList: ArrayList<RecordModel>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        instructionBox = view.findViewById(R.id.instructions_box)

        resultRecyclerView = view.findViewById(R.id.result_recycler_view)
        resultRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val sharedPref = activity?.getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val code = sharedPref?.getString(Constant.KEY_MED_CODE, "") ?: ""


        recordList = ArrayList()
        if (!code.isEmpty()) {
            getHistory(code)
            resultRecyclerView.adapter = ResultItemAdaper(context!!, recordList)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(TAG, "DEEEED")
    }

    fun convertToRecordModel(json: JSONObject): RecordModel {
        val categoryLabel = when (json.getString("resultado")) {
            "1" -> resources.getString(R.string.category_initial_label)
            "2" -> resources.getString(R.string.category_moderate_label)
            "3" -> resources.getString(R.string.category_advanced_label)
            else -> resources.getString(R.string.category_none_label)
        }
        return RecordModel(
            json.getString("_id"),
            json.getString("paciente"),
            json.getString(Constant.API_DATE_RESPONSE_FIELD),
            json.getString("resultado"),
            categoryLabel)
    }

    fun getHistory(code: String) {
        val ctx = context ?: return

        val request = JsonArrayRequest(Request.Method.GET, Constant.API_GET_HISTORY_URL.plus("?codigo=$code"), null, Response.Listener {
                response ->
            instructionBox.visibility = View.INVISIBLE
            try {
                for (i in 0 until response.length()) {
                    val record = convertToRecordModel(response.getJSONObject(i))
                    Log.v(TAG, "Object $i: $record")
                    recordList?.add(record)
                }
                resultRecyclerView.adapter?.notifyDataSetChanged()
            } catch (e: JSONException) {
                Log.v(TAG, "Error: ${e.printStackTrace()}")
            }
        }, Response.ErrorListener { error ->
            Log.v(TAG, "Error: ${error.printStackTrace()}")
        })

        ApiFetcher.getInstance(ctx).addToRequestQueue(request)
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

}
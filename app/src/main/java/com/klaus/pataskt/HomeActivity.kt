package com.klaus.pataskt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.klaus.pataskt.data.model.RecordModel
import com.klaus.pataskt.service.ApiFetcher
import com.klaus.pataskt.ui.account.AccountFragment
import com.klaus.pataskt.ui.account.AccountLoginFragment
import com.klaus.pataskt.ui.home.HomeFragment
import com.klaus.pataskt.ui.login.LoginFragment
import com.klaus.pataskt.util.Constant
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : AppCompatActivity(), LoginFragment.IAccountLoggin,
    AccountFragment.IAccountFragment, HomeFragment.IHomeFragment {

    var HOME_REQUEST_CODE = 10
    lateinit var navView: BottomNavigationView
    lateinit var code: String
    lateinit var recordList: ArrayList<RecordModel>
    lateinit var spinner: ProgressBar

    private fun startCameraActivity() {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivityForResult(intent, HOME_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navView = findViewById(R.id.nav_view)
        spinner = findViewById(R.id.progress_loader)
        //loadFragment(HomeFragment())

        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        code = sharedPref.getString(Constant.KEY_MED_CODE, "") ?: ""

        recordList = ArrayList()
        requestHistory(code, true)

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                }
                R.id.navigation_scanner -> {
                    startCameraActivity()
                }
                R.id.navigation_account -> {
                    loadFragment(getAccountFragmentToShow())
                }
            }
            true
        }

    }

    private fun getAccountFragmentToShow() : Fragment {
        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        code = sharedPref.getString(Constant.KEY_MED_CODE, "") ?: ""
        return if (code.isEmpty()) {
            AccountLoginFragment()
        } else {
            AccountFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        navView.selectedItemId = R.id.navigation_home
        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        code = sharedPref.getString(Constant.KEY_MED_CODE, "") ?: ""
        requestHistory(code, true)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.host_fragment, fragment).commit()
    }

    override fun onLogin() {
        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        code = sharedPref.getString(Constant.KEY_MED_CODE, "") ?: ""
        requestHistory(code, false)
        loadFragment(getAccountFragmentToShow())
    }

    override fun onLogout() {
        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        loadFragment(AccountLoginFragment())
    }

    override fun getHistory(): ArrayList<RecordModel> {
        return recordList
    }

    fun requestHistory(code: String, navigate: Boolean) {
        if (code.isEmpty()) {
            if (navigate) {
                loadFragment(HomeFragment())
            }
            return
        }
        showSpinner(true)

        val request = JsonArrayRequest(Request.Method.GET, Constant.API_GET_HISTORY_URL.plus("?codigo=$code"), null, Response.Listener {
                response ->
            try {
                for (i in 0 until response.length()) {
                    val record = convertToRecordModel(response.getJSONObject(i))
                    Log.v(TAG, "Object $i: $record")
                    recordList.add(record)
                }
            } catch (e: JSONException) {
                Log.v(TAG, "Error: ${e.printStackTrace()}")
            }
            showSpinner(false)
            if (navigate) {
                loadFragment(HomeFragment())
            }
        }, Response.ErrorListener { error ->
            Log.v(TAG, "Error: ${error.printStackTrace()}")
        })

        ApiFetcher.getInstance(this).addToRequestQueue(request)
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

    fun showSpinner(show: Boolean) {
        when (show) {
            true -> spinner.visibility = View.VISIBLE
            false -> spinner.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}

package com.klaus.pataskt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.klaus.pataskt.ui.account.AccountFragment
import com.klaus.pataskt.ui.account.AccountLoginFragment
import com.klaus.pataskt.ui.home.HomeFragment
import com.klaus.pataskt.ui.login.LoginFragment
import com.klaus.pataskt.util.Constant

class HomeActivity : AppCompatActivity(), LoginFragment.IAccountLoggin {

    var HOME_REQUEST_CODE = 10
    lateinit var navView: BottomNavigationView

    private fun startCameraActivity() {
        val intent = Intent(this, ScannerActivity::class.java)
        startActivityForResult(intent, HOME_REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navView = findViewById(R.id.nav_view)
        loadFragment(HomeFragment())

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
        val code = sharedPref.getString(Constant.KEY_MED_CODE, "") ?: ""
        return if (code.isEmpty()) {
            AccountLoginFragment()
        } else {
            AccountFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        navView.selectedItemId = R.id.navigation_home
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.host_fragment, fragment).commit()
    }

    override fun onLogin() {
        loadFragment(getAccountFragmentToShow())
    }
}

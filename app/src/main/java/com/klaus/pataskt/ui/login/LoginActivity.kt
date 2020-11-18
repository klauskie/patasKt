package com.klaus.pataskt.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.klaus.pataskt.HomeActivity

import com.klaus.pataskt.R
import com.klaus.pataskt.util.Constant

class LoginActivity : AppCompatActivity(), LoginFragment.IAccountLoggin {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadFragment(LoginFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.host_fragment, fragment).commit()
    }

    override fun onLogin() {
        setSharedFlag()
        //Complete and destroy login activity once successful
        setResult(Activity.RESULT_OK)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun setSharedFlag() {
        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(Constant.KEY_HAS_SEEN_LOGIN_SCREEN, true)
            apply()
        }
    }
}


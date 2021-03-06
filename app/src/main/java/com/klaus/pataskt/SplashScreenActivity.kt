package com.klaus.pataskt

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.klaus.pataskt.ui.login.LoginActivity
import com.klaus.pataskt.util.Constant

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val hasSeenLogin = sharedPref.getBoolean(Constant.KEY_HAS_SEEN_LOGIN_SCREEN, false)
            val code = sharedPref.getString(Constant.KEY_MED_CODE, "")
            //sharedPref.edit().clear().apply()

            val intent = when(hasSeenLogin || !code.isNullOrBlank()) {
                true -> Intent(this, HomeActivity::class.java)
                false -> Intent(this, LoginActivity::class.java)
            }

            Log.d("SplashScreenActivity", "Show login screen: ".plus(hasSeenLogin.toString()))

            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}

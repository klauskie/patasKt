package com.klaus.pataskt.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.klaus.pataskt.R
import com.klaus.pataskt.util.Constant

open class LoginFragment : Fragment() {

    private var TAG = LoginFragment::class.java.simpleName
    private lateinit var loginViewModel: LoginViewModel
    protected lateinit var skiplogin: Button
    protected lateinit var messageCode: TextView

    interface IAccountLoggin {
        fun onLogin()
    }

    private lateinit var logginListener: IAccountLoggin

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.login_view, container, false)

        val loginCode = root.findViewById<EditText>(R.id.med_code)
        val login = root.findViewById<Button>(R.id.login)
        skiplogin = root.findViewById(R.id.skip_login)
        messageCode = root.findViewById(R.id.code_message)
        val loading = root.findViewById<ProgressBar>(R.id.loading)


        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                loginCode.error = getString(loginState.usernameError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }

            saveCode(loginCode.text.toString())
            logginListener.onLogin()
        })

        loginCode.afterTextChanged {
            loginViewModel.loginDataChanged(
                loginCode.text.toString()
            )
        }

        login.setOnClickListener {
            loginViewModel.login(loginCode.text.toString())
        }

        skiplogin.setOnClickListener {
            logginListener.onLogin()
        }


        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IAccountLoggin) {
            logginListener = context
        } else {
            Log.d(TAG, "Activity must implement the IAccountLoggin interface")
        }
    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val ctx = activity?.applicationContext ?: return
        Toast.makeText(
            ctx,
            "$welcome",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val ctx = activity?.applicationContext ?: return
        Toast.makeText(ctx, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun saveCode(code: String) {
        val sharedPref = activity?.getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(Constant.KEY_MED_CODE, code)
            apply()
        }
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
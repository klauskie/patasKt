package com.klaus.pataskt.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.klaus.pataskt.data.LoginRepository
import com.klaus.pataskt.data.Result

import com.klaus.pataskt.R
import com.klaus.pataskt.data.model.DoctorModel
import com.klaus.pataskt.data.model.LoggedInUser
import com.klaus.pataskt.service.ApiFetcher
import com.klaus.pataskt.util.Constant
import org.json.JSONException
import org.json.JSONObject

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginOLD(code: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(code)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = result.data)
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_code)
        }  else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    fun login(context: Context?, code: String) {
        val ctx = context ?: return

        val params = JSONObject()
        params.put(Constant.API_CODE_REQUEST_FIELD, code)

        val request = JsonObjectRequest(Request.Method.POST, Constant.API_LOGIN_URL, params, Response.Listener {
                response ->
            try {
                Log.v(TAG, "Response: $response")
                val responseDoctor = response.getJSONObject("doctor")
                val doctorModel = DoctorModel(responseDoctor.getString(Constant.KEY_API_NAME), "",
                    responseDoctor.getString(Constant.KEY_DOCTOR_EMAIL),
                    responseDoctor.getString(Constant.KEY_DOCTOR_HOSPITAL))
                val loggedUser = LoggedInUser(response.getString(Constant.KEY_API_ID), response.getString(
                    Constant.KEY_API_NAME), response.getString(Constant.KEY_API_PHONE), code, doctorModel)
                loginRepository.setLoggedInUser(loggedUser)
                _loginResult.value = LoginResult(success = loggedUser)
            } catch (e: JSONException) {
                Log.v(TAG, "Error: ${e.printStackTrace()}")
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }, Response.ErrorListener { error ->
            Log.v(TAG, "Error: ${error.printStackTrace()}")
            _loginResult.value = LoginResult(error = R.string.login_failed)
        })

        ApiFetcher.getInstance(ctx).addToRequestQueue(request)
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

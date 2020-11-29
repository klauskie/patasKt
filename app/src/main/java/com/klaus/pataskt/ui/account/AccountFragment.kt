package com.klaus.pataskt.ui.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.klaus.pataskt.R
import com.klaus.pataskt.util.Constant

class AccountFragment : Fragment() {

    lateinit var accountListener: IAccountFragment

    interface IAccountFragment {
        fun onLogout()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val sharedPref = activity?.getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)

        val logoutBtn = view.findViewById<Button>(R.id.logout_btn)
        val userNameEdit = view.findViewById<TextView>(R.id.user_name_edit)
        val userPhoneEdit = view.findViewById<TextView>(R.id.user_phone_edit)
        val userCodeEdit = view.findViewById<TextView>(R.id.user_code_edit)
        val doctorNameEdit = view.findViewById<TextView>(R.id.doctor_name_edit)
        val doctorEmailEdit = view.findViewById<TextView>(R.id.doctor_email_edit)
        val doctorHospitalEdit = view.findViewById<TextView>(R.id.doctor_hospital_edit)

        userNameEdit.text = sharedPref?.getString(Constant.KEY_USER_NAME, "")
        userPhoneEdit.text = sharedPref?.getString(Constant.KEY_USER_PHONE, "")
        userCodeEdit.text = sharedPref?.getString(Constant.KEY_MED_CODE, "")
        doctorNameEdit.text = sharedPref?.getString(Constant.KEY_DOCTOR_NAME, "")
        doctorEmailEdit.text = sharedPref?.getString(Constant.KEY_DOCTOR_EMAIL, "")
        doctorHospitalEdit.text = sharedPref?.getString(Constant.KEY_DOCTOR_HOSPITAL, "")

        logoutBtn.setOnClickListener {
            accountListener.onLogout()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IAccountFragment) {
            accountListener = context
        } else {
            Log.d(TAG, "Activity must implement the IAccountFragment interface")
        }
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}
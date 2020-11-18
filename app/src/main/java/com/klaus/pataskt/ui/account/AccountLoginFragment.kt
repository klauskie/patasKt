package com.klaus.pataskt.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.klaus.pataskt.R
import com.klaus.pataskt.ui.login.*

class AccountLoginFragment : LoginFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        viewSetup()
        return root
    }

    private fun viewSetup() {
        skiplogin.visibility = View.INVISIBLE
        messageCode.text = getString(R.string.info_code_no_skip_label)
    }


}

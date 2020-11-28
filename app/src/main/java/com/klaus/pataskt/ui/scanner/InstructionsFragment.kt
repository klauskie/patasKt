package com.klaus.pataskt.ui.scanner


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.klaus.pataskt.R


class InstructionsFragment : Fragment() {

    private val TAG: String = InstructionsFragment::class.java.simpleName
    private lateinit var _instructionsFragment: InstructionsListener

    interface InstructionsListener {
        fun dismissInstructions()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)

        val okBtn = view.findViewById<Button>(R.id.ok_btn)
        okBtn.setOnClickListener {
            _instructionsFragment.dismissInstructions()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is InstructionsListener) {
            _instructionsFragment = context
        } else {
            Log.d(TAG, "Activity must implement the InstructionsListener interface")
        }
    }

}

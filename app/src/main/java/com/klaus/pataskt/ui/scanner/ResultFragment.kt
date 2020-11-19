package com.klaus.pataskt.ui.scanner

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.klaus.pataskt.R

class ResultFragment : Fragment() {

    private val TAG = ResultFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_result, container, false)

        arguments?.getParcelable<Bitmap>(ARG_IMAGE_BITMAP)?.let {
            Log.d(TAG, it.toString())
        }

        return view
    }

    companion object{
        private const val ARG_IMAGE_BITMAP = "ARG_IMAGE_BITMAP"

        fun newInstance(imageBM: Bitmap): ResultFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_IMAGE_BITMAP, imageBM)
            val fragment = ResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
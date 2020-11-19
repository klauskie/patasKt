package com.klaus.pataskt.ui.scanner

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.klaus.pataskt.R
import kotlinx.android.synthetic.main.fragment_photo_result.close_btn
import kotlinx.android.synthetic.main.fragment_photo_result.imageViewResult
import kotlinx.android.synthetic.main.fragment_photo_result.next_btn

class PhotoResultFragment : Fragment() {

    private val TAG = PhotoResultFragment::class.java.simpleName
    private lateinit var photoResultListener: IPhotoResultFragment

    interface IPhotoResultFragment {
        fun onPhotoClosed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_result, container, false)

        arguments?.getParcelable<Bitmap>(ARG_IMAGE_BITMAP)?.let {
            imageViewResult.setImageBitmap(it)
        }

        close_btn.setOnClickListener {
            photoResultListener.onPhotoClosed()
        }

        next_btn.setOnClickListener {
            // TODO: Send Image, go to another fragment and wait for results
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IPhotoResultFragment) {
            photoResultListener = context
        } else {
            Log.d(TAG, "Activity must implement the IPhotoResultFragment interface")
        }
    }

    companion object{
        private const val ARG_IMAGE_BITMAP = "ARG_IMAGE_BITMAP"

        fun newInstance(imageBM: Bitmap): PhotoResultFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_IMAGE_BITMAP, imageBM)
            val fragment = PhotoResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.klaus.pataskt.ui.scanner

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.klaus.pataskt.R
import com.klaus.pataskt.service.ApiFetcher
import com.klaus.pataskt.util.Constant
import kotlinx.android.synthetic.main.fragment_photo_result.*
import org.json.JSONException
import org.json.JSONObject

class ResultFragment : Fragment() {

    private val TAG = ResultFragment::class.java.simpleName
    lateinit var resultContainer: LinearLayout
    lateinit var loaderContainer: RelativeLayout
    lateinit var doneButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_result, container, false)

        arguments?.getParcelable<Bitmap>(ARG_IMAGE_BITMAP_BASE64)?.let {
            Log.d(TAG, it.toString())
        }

        resultContainer = view.findViewById(R.id.result_container)
        loaderContainer = view.findViewById(R.id.loading_container)
        doneButton = view.findViewById(R.id.done_btn)

        doneButton.setOnClickListener {
            activity?.finish()
        }

        resultContainer.visibility = View.INVISIBLE

        val encodedImage = arguments?.getString(ARG_IMAGE_BITMAP_BASE64) ?: ""
        //postPhoto(encodedImage)
        getSample(encodedImage)

        return view
    }

    private fun postPhoto(encodedImage: String) {
        val ctx = context ?: return
        val params = JSONObject()
        params.put("photo_base_64", encodedImage)

        val request = JsonObjectRequest(Request.Method.POST, Constant.API_PHOTO_POST_URL, params, Response.Listener {
                response ->
            try {
                val jsonArray = response.getJSONArray("data")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> error.printStackTrace() })

        ApiFetcher.getInstance(ctx).addToRequestQueue(request)
    }

    private fun getSample(encodedImage: String) {
        val ctx = context ?: return
        val url = "https://www.google.com"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                results_label.visibility = View.VISIBLE
                loaderContainer.visibility = View.INVISIBLE
                resultContainer.visibility = View.VISIBLE
                doneButton.visibility = View.VISIBLE
                // Display the first 500 characters of the response string.
                //test_text.text = "Response is: ${response.substring(0, 500)}"
                //test_text_2.text = "Encoded image is: ${encodedImage.substring(0, 50)}"
            },
            Response.ErrorListener { error -> error.printStackTrace() })

        ApiFetcher.getInstance(ctx).addToRequestQueue(stringRequest)
    }

    companion object{
        private const val ARG_IMAGE_BITMAP_BASE64 = "ARG_IMAGE_BITMAP_BASE64"

        fun newInstance(imageBM: Bitmap): ResultFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_IMAGE_BITMAP_BASE64, imageBM)
            val fragment = ResultFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(encodedImage: String): ResultFragment {
            val args: Bundle = Bundle()
            args.putString(ARG_IMAGE_BITMAP_BASE64, encodedImage)
            val fragment = ResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
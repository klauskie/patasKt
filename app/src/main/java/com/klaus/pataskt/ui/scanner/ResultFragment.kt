package com.klaus.pataskt.ui.scanner

import android.content.Context
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
    lateinit var retryButton: Button
    lateinit var _code: String
    lateinit var resultListener: IResultFragment

    interface IResultFragment {
        fun goToCamera()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_result, container, false)

        resultContainer = view.findViewById(R.id.result_container)
        loaderContainer = view.findViewById(R.id.loading_container)
        doneButton = view.findViewById(R.id.done_btn)
        retryButton = view.findViewById(R.id.retry_btn)

        doneButton.setOnClickListener {
            activity?.finish()
        }

        retryButton.setOnClickListener {
            resultListener.goToCamera()
        }

        val encodedImage = arguments?.getString(ARG_IMAGE_BITMAP_BASE64) ?: ""
        Log.v(TAG, "Encoded image: $encodedImage")

        val sharedPref = activity?.getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        _code = sharedPref?.getString(Constant.KEY_MED_CODE, "") ?: ""

        // Send Image
        postPhoto(encodedImage)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IResultFragment) {
            resultListener = context
        } else {
            Log.d(TAG, "Activity must implement the IResultFragment interface")
        }
    }

    private fun handleResponse(category: String, date: String, status: Int) {
        loaderContainer.visibility = View.INVISIBLE

        if (status == STATUS_OK) {
            results_label.visibility = View.VISIBLE
            resultContainer.visibility = View.VISIBLE
            doneButton.visibility = View.VISIBLE
            date_edit.text = date
            when (category) {
                CATEGORY_ONE -> {
                    image_result.setImageResource(R.drawable.result_image_initial)
                    status_edit.setText(R.string.category_initial_label)
                }
                CATEGORY_TWO -> {
                    image_result.setImageResource(R.drawable.result_image_moderate)
                    status_edit.setText(R.string.category_moderate_label)
                }
                CATEGORY_THREE -> {
                    image_result.setImageResource(R.drawable.result_image_advanced)
                    status_edit.setText(R.string.category_advanced_label)
                }
                else -> status_edit.setText(R.string.category_none_label)
            }
        } else {
            // TODO: display correct error
            error_container.visibility = View.VISIBLE
        }
    }

    private fun postPhoto(encodedImage: String) {
        val ctx = context ?: return

        val params = JSONObject()
        params.put(Constant.API_PHOTO_POST_REQUEST_FIELD, encodedImage)
        if (_code.isNotEmpty()) {
            params.put(Constant.API_CODE_REQUEST_FIELD, _code)
        }

        val request = JsonObjectRequest(Request.Method.POST, Constant.API_PHOTO_POST_URL, params, Response.Listener {
                response ->
            try {
                Log.v(TAG, "Response: $response")
                val category = response.get(Constant.API_CATEGORY_RESPONSE_FIELD)
                val date = response.get(Constant.API_DATE_RESPONSE_FIELD)
                handleResponse(category.toString(), date.toString(), STATUS_OK)
            } catch (e: JSONException) {
                Log.v(TAG, "Error: ${e.printStackTrace()}")
                handleResponse("", "", STATUS_ERROR)
            }
        }, Response.ErrorListener { error ->
            Log.v(TAG, "Error: ${error.printStackTrace()}")
            handleResponse("", "", STATUS_ERROR)
        })

        ApiFetcher.getInstance(ctx).addToRequestQueue(request)
    }

    companion object{
        private const val ARG_IMAGE_BITMAP_BASE64 = "ARG_IMAGE_BITMAP_BASE64"
        private const val STATUS_OK = 0
        private const val STATUS_ERROR = 1
        private const val CATEGORY_ONE = "1"
        private const val CATEGORY_TWO = "2"
        private const val CATEGORY_THREE = "3"

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
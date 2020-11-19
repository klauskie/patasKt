package com.klaus.pataskt.ui.scanner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.klaus.pataskt.R
import kotlinx.android.synthetic.main.fragment_camera.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {

    private val TAG: String = CameraFragment::class.java.simpleName

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var cameraListener: ICameraFragment

    interface ICameraFragment {
        fun onPhotoTaken(image: Bitmap)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        startCamera()

        // Set up the listener for take photo button
        val cameraBtn = view.findViewById<Button>(R.id.camera_capture_button)
        cameraBtn.setOnClickListener { takePhoto() }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ICameraFragment) {
            cameraListener = context
        } else {
            Log.d(TAG, "Activity must implement the ICameraFragment interface")
        }
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        cameraProvider.unbindAll()
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    Log.d(TAG, "takePhoto(); Photo capture succeeded")

                    // display image
                    val imageBitMap = image.ToBitmap()
                    cameraListener.onPhotoTaken(imageBitMap)
                    image.close()
                }

            })
    }

    private fun startCamera() {
        val ctx = context ?: return
        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    fun ImageProxy.ToBitmap(): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

}
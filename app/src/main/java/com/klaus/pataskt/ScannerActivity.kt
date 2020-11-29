package com.klaus.pataskt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.klaus.pataskt.ui.scanner.CameraFragment
import com.klaus.pataskt.ui.scanner.InstructionsFragment
import com.klaus.pataskt.ui.scanner.ResultFragment
import com.klaus.pataskt.util.Constant
import java.io.ByteArrayOutputStream

class ScannerActivity : AppCompatActivity(), CameraFragment.ICameraFragment, InstructionsFragment.InstructionsListener, ResultFragment.IResultFragment {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        // Request camera permissions
        if (allPermissionsGranted()) {
            showFirstFragment()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                showFirstFragment()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onPhotoTaken(image: Bitmap) {
        val encodedImage = encodeImage(image)
        Log.d(TAG, "Encoded image: $encodedImage")
        loadFragment(ResultFragment.newInstance(encodedImage?: ""))
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.host_fragment, fragment).commit()
    }

    private fun showFirstFragment() {
        val sharedPref = getSharedPreferences(Constant.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val hasSeenInstructions = sharedPref.getBoolean(Constant.KEY_HAS_SEEN_CAMERA_INSTRUCTIONS, false)
        if (hasSeenInstructions) {
            loadFragment(CameraFragment())
        } else {
            sharedPref.edit().putBoolean(Constant.KEY_HAS_SEEN_CAMERA_INSTRUCTIONS, true).apply()
            loadFragment(InstructionsFragment())
        }
    }

    override fun showInfo() {
        loadFragment(InstructionsFragment())
    }

    override fun dismissInstructions() {
        loadFragment(CameraFragment())
    }

    override fun goToCamera() {
        loadFragment(InstructionsFragment())
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    companion object {
        private const val TAG = "ScannerActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

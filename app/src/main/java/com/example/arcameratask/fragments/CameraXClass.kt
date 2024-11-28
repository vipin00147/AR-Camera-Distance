package com.example.arcameratask.fragments

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.arcameratask.base.ArCoreTask
import com.example.arcameratask.base.BaseFragment
import com.example.arcameratask.databinding.FragmentCameraxBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

const val REQUEST_CODE_PERMISSIONS = 101
@ExperimentalGetImage
class CameraXClass : BaseFragment<FragmentCameraxBinding>() {

    private var previewView: PreviewView ?= null
    private var imageAnalysis: ImageAnalysis? = null
    private var imageCapture: ImageCapture ?= null

    private var captured = false
    private val handler = Handler(Looper.getMainLooper())
    private val saveInterval = 3000L // 3 seconds
    private val executor = Executors.newSingleThreadExecutor()

    private val TAG = "ddckhbdkbsdkcbskjdcbkjdsbcjdbckjbdskcjbsdkjcbkjsdcbkjdsc"


    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCameraxBinding {
        return FragmentCameraxBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBinding()?.let {
            with(it) {
                this@CameraXClass.previewView = previewView

                if (ContextCompat.checkSelfPermission(getBaseActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCamera()

                    Handler().postDelayed({
                        takePicture()
                    },3000)

                } else {
                    ActivityCompat.requestPermissions(getBaseActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_PERMISSIONS)
                }
            }
        }
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(getBaseActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(getBinding()?.previewView?.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(getBaseActivity()))
    }


    private fun takePicture() {
        // Create output directory
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "CameraXPhotos"
        )
        if (!storageDir.exists()) storageDir.mkdirs()

        // Create file for captured image
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val photoFile = File(storageDir, "IMG_$timeStamp.jpg")

        // Set up output options
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Take a picture
        imageCapture?.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    getBaseActivity().runOnUiThread {
                        Handler().postDelayed({
                            takePicture()
                        },3000)
                        Toast.makeText(getBaseActivity(), "Image saved: ${photoFile.absolutePath}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    getBaseActivity().runOnUiThread {
                        Toast.makeText(getBaseActivity(), "Failed to save image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}
package com.example.arcameratask.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.arcameratask.R
import com.example.arcameratask.base.BaseFragment
import com.example.arcameratask.databinding.FragmentQRCodeScanBinding
import com.google.android.material.snackbar.Snackbar
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.BarcodeFormat
import io.github.g00fy2.quickie.config.ScannerConfig
import io.github.g00fy2.quickie.content.QRContent

class QRCodeScanFragment : BaseFragment<FragmentQRCodeScanBinding>() {

    private var isCameraLaunched = false

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentQRCodeScanBinding {
        return FragmentQRCodeScanBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBinding()?.let {
            with(it) {

                btnStartQrCodeScan.setOnClickListener {
                    launchQrCodeScanner()
                }

                if(!isCameraLaunched) {
                    isCameraLaunched = true
                    launchQrCodeScanner()
                }
            }
        }
    }

    private fun launchQrCodeScanner() {
        scanCustomCode.launch(
            ScannerConfig.build {
                setBarcodeFormats(listOf(BarcodeFormat.FORMAT_QR_CODE)) // set interested barcode formats
                setOverlayStringRes(R.string.scan_barcode) // string resource used for the scanner overlay
                setHapticSuccessFeedback(false) // enable (default) or disable haptic feedback when a barcode was detected
                setShowTorchToggle(true) // show or hide (default) torch/flashlight toggle button
                setShowCloseButton(true) // show or hide (default) close button
                setUseFrontCamera(false) // use the front camera
                setKeepScreenOn(true) // keep the device's screen turned on
            }
        )
    }

    private val scanCustomCode = registerForActivityResult(ScanCustomCode(), ::showSnackbar)

    private fun showSnackbar(result: QRResult) {
        val text = when (result) {
            is QRResult.QRSuccess -> {

                result.content.rawValue
                // decoding with default UTF-8 charset when rawValue is null will not result in meaningful output, demo purpose
                    ?: result.content.rawBytes?.let { String(it) }.orEmpty()
            }
            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> "Missing permission"
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
        }

        getBinding()?.root?.let {
            Snackbar.make(it, text, Snackbar.LENGTH_INDEFINITE).apply {
                view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)?.run {
                    maxLines = 5
                    setTextIsSelectable(true)
                }
                if (result is QRResult.QRSuccess) {
                    val content = result.content
                    if (content is QRContent.Url) {
                        setAction(R.string.open_action) {
                            openUrl(content.url)
                            dismiss()
                        }
                        return@apply
                    }
                }
                setAction(R.string.ok_action) {
                    dismiss()
                }
            }.show()
        }
    }

    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            // no Activity found to run the given Intent
        }
    }
}
package com.example.arcameratask.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.arcameratask.R
import com.example.arcameratask.activities.MeasurementActivity
import com.example.arcameratask.activities.ROOT_CONTAINER_ID
import com.example.arcameratask.base.BaseFragment
import com.example.arcameratask.databinding.FragmentMainBinding
import com.example.arcameratask.utils.changeFragment

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBinding()?.let {
            with(it) {
                btnCameraDistance.setOnClickListener {
                    getBaseActivity().startIntentActivity<MeasurementActivity<Any>>(isFinish = false)
                }

                btnStartQrCodeScan.setOnClickListener {

                    //QRCodeScanFragment().changeFragment(ROOT_CONTAINER_ID, getBaseActivity(), true)
                    CameraXClass().changeFragment(ROOT_CONTAINER_ID, getBaseActivity(), true)
                }
            }
        }
    }
}

inline fun <reified T : AppCompatActivity> AppCompatActivity.startIntentActivity(isFinish: Boolean) {

    Intent(this, T::class.java).apply {
        this@startIntentActivity.startActivity(this)
    }

    if (isFinish) {
        finish()
    }
}
package com.example.arcameratask.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.arcameratask.R
import com.example.arcameratask.activities.MeasurementActivity
import com.example.arcameratask.base.BaseFragment
import com.example.arcameratask.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val buttonArrayList = ArrayList<String>()

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonArray = resources.getStringArray(R.array.arcore_measurement_buttons)

        buttonArray.map{it->
            buttonArrayList.add(it)
        }

        getBinding()?.let {
            with(it) {
                toMeasurement.text = buttonArrayList[0]

                toMeasurement.setOnClickListener {
                    getBaseActivity().startIntentActivity<MeasurementActivity<Any>>(isFinish = true)
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
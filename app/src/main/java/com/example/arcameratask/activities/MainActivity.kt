package com.example.arcameratask.activities

import android.os.Bundle
import com.example.arcameratask.R
import com.example.arcameratask.base.BaseActivity
import com.example.arcameratask.databinding.ActivityMainBinding
import com.example.arcameratask.fragments.MainFragment
import com.example.arcameratask.utils.changeFragment

var ROOT_CONTAINER_ID = 0
class MainActivity<T> : BaseActivity<ActivityMainBinding>() {

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ROOT_CONTAINER_ID = getBinding()?.rootContainer?.id!!

        MainFragment().changeFragment(ROOT_CONTAINER_ID, this, false)
    }

}
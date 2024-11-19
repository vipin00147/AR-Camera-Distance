package com.example.arcameratask.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    private var viewDataBinding: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = createBinding()
        setContentView(viewDataBinding?.root)

    }


    fun getBinding() = viewDataBinding

    abstract fun createBinding(): T

}
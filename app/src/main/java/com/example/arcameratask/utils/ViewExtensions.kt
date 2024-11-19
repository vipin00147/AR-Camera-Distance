package com.example.arcameratask.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Fragment.changeFragment(
    containerId: Int,
    requireActivity: FragmentActivity,
    isAddToBakStack: Boolean,
) {

    val transaction = requireActivity.supportFragmentManager.beginTransaction()

    transaction.replace(containerId, this)
    if (isAddToBakStack) {
        transaction.addToBackStack(this::class.java.simpleName)
    }

    transaction.commitAllowingStateLoss()

}
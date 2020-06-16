package com.kapil.presentation.home

import androidx.annotation.StringRes
import com.kapil.presentation.base.BaseFragment

abstract class BottomNavFragment : BaseFragment() {

    @StringRes
    abstract fun getTitle(): Int
}
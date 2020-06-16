package com.kapil.presentation.base

import androidx.annotation.StringRes

interface BaseView {

    fun showMessage(@StringRes messageResId: Int)

    fun closeKeyboard()

    fun showDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        positiveAction: () -> Unit
    )
}
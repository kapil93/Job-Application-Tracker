package com.kapil.presentation.common

interface ErrorView {
    fun updateErrorView(isError: Boolean, retry: (() -> Unit))
}
package com.kapil.presentation

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class WinterviewTestJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, WinterviewTestApp::class.java.name, context)
}
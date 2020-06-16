package com.kapil.winterview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kapil.presentation.home.HomeActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {

    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onStart() {
        super.onStart()
        // A delay is added so that the splash screen is visible
        disposable = Completable.complete()
            .delay(
                resources.getInteger(R.integer.splash_screen_duration_millis).toLong(),
                TimeUnit.MILLISECONDS
            )
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, {
                finish()
            })
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }
}

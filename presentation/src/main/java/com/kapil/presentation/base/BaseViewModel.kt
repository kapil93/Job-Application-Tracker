package com.kapil.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


abstract class BaseViewModel : ViewModel() {

    protected val disposable = CompositeDisposable()

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isError = MutableLiveData<Pair<Boolean, (() -> Unit)>>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isError: LiveData<Pair<Boolean, (() -> Unit)>> = _isError

    protected fun Completable.applySchedulers(): Completable = compose {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> Single<T>.applySchedulers(): Single<T> = compose {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> Flowable<T>.applySchedulers(): Flowable<T> = compose {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * The specific use case of this method is to run a transformer block, having both upstream and
     * downstream executing on main thread, on a background thread.
     *
     * It takes control from main thread and only executes the transformer block on a background
     * thread and returns controls to the main thread.
     */
    private fun <T, R> Flowable<T>.applySchedulersTo(transformer: Flowable<T>.() -> Flowable<R>): Flowable<R> =
        compose {
            it.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .transformer()
                .observeOn(AndroidSchedulers.mainThread())
        }

    protected fun Completable.applyLoadingObservable(): Completable = compose {
        it.doOnSubscribe { _isLoading.value = true }
            .doAfterTerminate { _isLoading.value = false }
            .doOnDispose { _isLoading.value = false }
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> Single<T>.applyLoadingObservable(): Single<T> = compose {
        it.doOnSubscribe { _isLoading.value = true }
            .doAfterTerminate { _isLoading.value = false }
            .doOnDispose { _isLoading.value = false }
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> Flowable<T>.applyLoadingObservable(): Flowable<T> = compose {
        it.doOnSubscribe { _isLoading.value = true }
            .doOnNext { _isLoading.value = false }
            .doAfterTerminate { _isLoading.value = false }
            .doOnCancel { _isLoading.value = false }
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    /**
     * This method enables the loading view to be visible during successive emissions of flowable
     * and not only during subscription. The transformer block is expected to contain the heavy task
     * and will be executed on a background thread.
     */
    protected fun <T, R> Flowable<T>.applySchedulersAndLoadingObservableTo(transformer: Flowable<T>.() -> Flowable<R>): Flowable<R> =
        compose {
            it.doOnSubscribe { _isLoading.value = true }
                .doOnNext { _isLoading.value = true }
                .subscribeOn(AndroidSchedulers.mainThread())
                .applySchedulersTo(transformer)
                .doOnNext { _isLoading.value = false }
                .doAfterTerminate { _isLoading.value = false }
                .doOnCancel { _isLoading.value = false }
        }

    protected fun Completable.applyErrorObservable(retry: (() -> Unit)): Completable = compose {
        it.doOnError { _isError.value = Pair(true, retry) }
            .doOnComplete { _isError.value = Pair(false, {}) }
            .doOnDispose { _isError.value = Pair(false, {}) }
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> Single<T>.applyErrorObservable(retry: (() -> Unit)): Single<T> = compose {
        it.doOnError { _isError.value = Pair(true, retry) }
            .doOnSuccess { _isError.value = Pair(false, {}) }
            .doOnDispose { _isError.value = Pair(false, {}) }
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    protected fun <T> Flowable<T>.applyErrorObservable(retry: (() -> Unit)): Flowable<T> = compose {
        it.doOnError { _isError.value = Pair(true, retry) }
            .doOnNext { _isError.value = Pair(false, {}) }
            .doOnCancel { _isError.value = Pair(false, {}) }
            .subscribeOn(AndroidSchedulers.mainThread())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
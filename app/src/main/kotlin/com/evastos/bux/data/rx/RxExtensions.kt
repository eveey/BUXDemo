package com.evastos.bux.data.rx

import com.evastos.bux.data.exception.ExceptionMapper
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Single<T>.applySchedulers(rxSchedulers: RxSchedulers): Single<T> {
    return this.subscribeOn(rxSchedulers.ioScheduler).observeOn(rxSchedulers.mainThreadScheduler)
}

fun <T> Observable<T>.applySchedulers(rxSchedulers: RxSchedulers): Observable<T> {
    return this.subscribeOn(rxSchedulers.ioScheduler).observeOn(rxSchedulers.mainThreadScheduler)
}

fun <T> Flowable<T>.applySchedulers(rxSchedulers: RxSchedulers): Flowable<T> {
    return this.subscribeOn(rxSchedulers.ioScheduler).observeOn(rxSchedulers.mainThreadScheduler)
}

fun <T, E : Throwable> Single<T>.mapException(exceptionMapper: ExceptionMapper<E>): Single<T> {
    return retryWhen {
        return@retryWhen it.flatMap { throwable ->
            Flowable.error<Throwable> {
                exceptionMapper.map(throwable)
            }
        }
    }
}

fun <T, E : Throwable> Flowable<T>.mapException(exceptionMapper: ExceptionMapper<E>): Flowable<T> {
    return retryWhen {
        return@retryWhen it.flatMap { throwable ->
            Flowable.error<Throwable> {
                exceptionMapper.map(throwable)
            }
        }
    }
}

fun <T> Flowable<T>.throttleLastMillis(intervalDurationMillis: Long): Flowable<T> {
    return throttleLast(intervalDurationMillis, TimeUnit.MILLISECONDS, Schedulers.computation())
}

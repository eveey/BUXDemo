package com.evastos.bux.ui.base

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.evastos.bux.RxTestSchedulerRule
import com.evastos.bux.TestUtil
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BaseViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxTestSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val networkConnectivityLiveDataObserver = mock<Observer<Boolean>>()
    private val networkConnectivitySubject = PublishSubject.create<Boolean>()

    private lateinit var viewModel: TestViewModel

    @Before
    fun setUp() {
        viewModel = TestViewModel()
        viewModel.networkConnectivityLiveData.observeForever(networkConnectivityLiveDataObserver)
    }

    @Test
    fun getNetworkConnectivityLiveData_withNetworkConnectivityEvents_postsDistinctValues() {
        viewModel.observeNetworkConnectivity(networkConnectivitySubject)

        networkConnectivitySubject.onNext(true)
        networkConnectivitySubject.onNext(true)
        networkConnectivitySubject.onNext(false)
        networkConnectivitySubject.onNext(true)
        networkConnectivitySubject.onNext(false)
        networkConnectivitySubject.onNext(false)

        verify(networkConnectivityLiveDataObserver, times(2)).onChanged(true)
        verify(networkConnectivityLiveDataObserver, times(2)).onChanged(false)
        verifyNoMoreInteractions(networkConnectivityLiveDataObserver)
    }

    @Test
    fun getNetworkConnectivityLiveData_withNoNetworkConnectivityEvents_doesNotPostAnything() {
        viewModel.observeNetworkConnectivity(networkConnectivitySubject)

        verifyNoMoreInteractions(networkConnectivityLiveDataObserver)
    }

    private inner class TestViewModel : BaseViewModel(TestUtil.rxSchedulers)
}

package com.nit.koudaisai.presentation.splash

import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.presentation.KoudaisaiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val logService: LogService
) : KoudaisaiViewModel(logService) {

    fun onAppStart(popUpToHome: () -> Unit) {
        popUpToHome()
    }
}
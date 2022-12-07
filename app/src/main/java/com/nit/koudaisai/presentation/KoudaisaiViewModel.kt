package com.nit.koudaisai.presentation

import androidx.lifecycle.ViewModel
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.nit.koudaisai.domain.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler

open class KoudaisaiViewModel(private val logService: LogService) : ViewModel() {
    open val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    open val logErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    open fun onError(error: Throwable) {
        SnackbarManager.showMessage(error.toSnackbarMessage())
        logService.logNonFatalCrash(error)
    }
}
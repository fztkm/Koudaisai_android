package com.nit.koudaisai.domain.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
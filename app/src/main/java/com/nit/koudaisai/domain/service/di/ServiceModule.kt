package com.nit.koudaisai.domain.service.di

import com.nit.koudaisai.domain.service.AccountService
import com.nit.koudaisai.domain.service.AccountStorageService
import com.nit.koudaisai.domain.service.LogService
import com.nit.koudaisai.domain.service.impl.FirebaseAccountServiceImpl
import com.nit.koudaisai.domain.service.impl.FirebaseLogServiceImpl
import com.nit.koudaisai.domain.service.impl.FirestoreAccountStorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: FirebaseAccountServiceImpl): AccountService

    @Binds
    abstract fun provideAccountStorageService(impl: FirestoreAccountStorageServiceImpl): AccountStorageService

    @Binds
    abstract fun provideLogService(impl: FirebaseLogServiceImpl): LogService
}
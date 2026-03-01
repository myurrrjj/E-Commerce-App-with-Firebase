package com.example.koshi.dependency

import com.example.koshi.repository.DiseaseRepository
import com.example.koshi.repository.NetworkDiseaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiseaseRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDiseaseRepository(impl: NetworkDiseaseRepository): DiseaseRepository
}
package com.example.koshi.dependency

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import com.example.koshi.data.CartRepository
import com.example.koshi.data.OfflineCartRepository
import com.example.koshi.repository.OfflinePlantRepository
import com.example.koshi.repository.PlantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePlantRepository(
        applicationScope: CoroutineScope,

    ): PlantRepository {

        return OfflinePlantRepository(
            applicationScope,

        )
    }

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository {
        return OfflineCartRepository()
    }

//    @Provides
//    @Singleton
//    fun provideImageLoader(
//        @ApplicationContext context: Context
//    ): ImageLoader {
//        return ImageLoader.Builder(context)
//            .diskCache {
//                DiskCache.Builder()
//                    .directory(context.cacheDir.resolve("image_cache"))
//                    .maxSizePercent(0.1)
//                    .build()
//            }
//            .respectCacheHeaders(false)
//            .build()
//    }


}
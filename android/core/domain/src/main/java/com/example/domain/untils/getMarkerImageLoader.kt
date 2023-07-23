package com.example.domain.untils

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy

fun getMarkerImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .networkCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.2)
                .build()
        }
        .respectCacheHeaders(false)
        .allowHardware(false)
        .build()
}
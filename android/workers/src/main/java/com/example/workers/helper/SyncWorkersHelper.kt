package com.example.workers.helper

import androidx.work.Constraints
import androidx.work.NetworkType

val syncWorkersConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
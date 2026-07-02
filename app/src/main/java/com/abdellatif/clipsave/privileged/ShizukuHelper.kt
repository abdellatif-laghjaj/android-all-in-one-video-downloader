package com.abdellatif.clipsave.privileged

import rikka.shizuku.Shizuku

/** Minimal Shizuku availability/permission status (no privileged ops required for Download/ writes). */
object ShizukuHelper {

    fun isAvailable(): Boolean = runCatching { Shizuku.pingBinder() }.getOrDefault(false)

    fun hasPermission(): Boolean = runCatching {
        Shizuku.pingBinder() && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
    }.getOrDefault(false)

    fun requestPermission(requestCode: Int) {
        runCatching { if (Shizuku.pingBinder()) Shizuku.requestPermission(requestCode) }
    }

    fun statusText(): String = when {
        !isAvailable() -> "Not running"
        hasPermission() -> "Connected & authorized"
        else -> "Running — tap to authorize"
    }
}

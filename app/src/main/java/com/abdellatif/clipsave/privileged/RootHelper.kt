package com.abdellatif.clipsave.privileged

import java.io.DataOutputStream

/** Lightweight root detection via `su` — avoids extra native deps. */
object RootHelper {

    fun isRootAvailable(): Boolean = runCatching {
        val process = ProcessBuilder("su", "-c", "id").redirectErrorStream(true).start()
        DataOutputStream(process.outputStream).use { it.writeBytes("exit\n"); it.flush() }
        val ok = process.waitFor() == 0
        ok
    }.getOrDefault(false)

    fun statusText(): String = if (isRootAvailable()) "Root access granted" else "Not rooted / denied"
}

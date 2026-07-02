package com.abdellatif.clipsave.access

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.abdellatif.clipsave.download.DownloadService
import com.abdellatif.clipsave.ui.MainActivity

/**
 * Shows a one-tap floating download bubble in supported apps.
 * On tap: if a link is visible on screen (Twitter/Reddit/etc.) it downloads immediately.
 * Otherwise (Instagram/TikTok hide URLs) it opens ClipSave in "quick grab" mode, which reads
 * the link you copied via the app's ••• -> "Copy link".
 */
class MediaAccessibilityService : AccessibilityService() {

    private var controller: FloatingButtonController? = null
    private var currentPackage: String = ""

    private val targets = setOf(
        "com.instagram.android",
        "com.zhiliaoapp.musically",
        "com.ss.android.ugc.trill",
        "com.twitter.android",
        "com.facebook.katana",
        "com.pinterest"
    )

    override fun onServiceConnected() {
        super.onServiceConnected()
        controller = FloatingButtonController(this) { onButtonTap() }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val pkg = event?.packageName?.toString() ?: return
        if (pkg == currentPackage) return
        currentPackage = pkg
        if (pkg in targets) controller?.show() else controller?.hide()
    }

    private fun onButtonTap() {
        val url = findUrl(rootInActiveWindow)
        if (url != null) {
            DownloadService.start(this, url)
            Toast.makeText(this, "ClipSave: downloading…", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Opening ClipSave — grabbing the copied link…", Toast.LENGTH_SHORT).show()
            val launch = Intent(this, MainActivity::class.java).apply {
                action = MainActivity.ACTION_QUICK_GRAB
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            runCatching { startActivity(launch) }
        }
    }

    private fun findUrl(node: AccessibilityNodeInfo?): String? {
        if (node == null) return null
        val regex = Regex("https?://[^\\s\"']+")
        val texts = listOfNotNull(node.text?.toString(), node.contentDescription?.toString())
        for (t in texts) regex.find(t)?.value?.let { return it.trimEnd('.', ',', ')') }
        for (i in 0 until node.childCount) findUrl(node.getChild(i))?.let { return it }
        return null
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        controller?.hide(); controller = null
        super.onDestroy()
    }
}

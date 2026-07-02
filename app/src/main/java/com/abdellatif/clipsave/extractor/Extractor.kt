package com.abdellatif.clipsave.extractor

import com.abdellatif.clipsave.data.model.Platform

/** Strategy interface for turning a page URL into a direct media URL. */
interface Extractor {
    val platform: Platform
    fun canHandle(url: String): Boolean

    /** @return at least one MediaInfo, or throws ExtractionException. */
    fun extract(url: String): List<MediaInfo>
}

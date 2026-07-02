package com.abdellatif.clipsave.extractor

import com.abdellatif.clipsave.extractor.extractors.FacebookExtractor
import com.abdellatif.clipsave.extractor.extractors.GenericExtractor
import com.abdellatif.clipsave.extractor.extractors.InstagramExtractor
import com.abdellatif.clipsave.extractor.extractors.PinterestExtractor
import com.abdellatif.clipsave.extractor.extractors.RedditExtractor
import com.abdellatif.clipsave.extractor.extractors.TelegramExtractor
import com.abdellatif.clipsave.extractor.extractors.TikTokExtractor
import com.abdellatif.clipsave.extractor.extractors.TwitterExtractor
import com.abdellatif.clipsave.extractor.extractors.YouTubeExtractor

/**
 * Lightweight meta-scraper fallback used when the yt-dlp engine can't handle a URL
 * (e.g. pure image posts). [GenericExtractor] is always last and handles anything.
 */
object ExtractorRegistry {
    private val extractors: List<Extractor> = listOf(
        RedditExtractor(),
        InstagramExtractor(),
        TikTokExtractor(),
        TwitterExtractor(),
        FacebookExtractor(),
        PinterestExtractor(),
        TelegramExtractor(),
        YouTubeExtractor(),
        GenericExtractor()
    )

    fun extract(url: String): List<MediaInfo> {
        val handler = extractors.firstOrNull { it.canHandle(url) } ?: GenericExtractor()
        return handler.extract(url)
    }
}

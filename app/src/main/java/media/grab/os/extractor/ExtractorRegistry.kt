package media.grab.os.extractor

import media.grab.os.extractor.extractors.FacebookExtractor
import media.grab.os.extractor.extractors.GenericExtractor
import media.grab.os.extractor.extractors.InstagramExtractor
import media.grab.os.extractor.extractors.PinterestExtractor
import media.grab.os.extractor.extractors.RedditExtractor
import media.grab.os.extractor.extractors.TelegramExtractor
import media.grab.os.extractor.extractors.TikTokExtractor
import media.grab.os.extractor.extractors.TwitterExtractor
import media.grab.os.extractor.extractors.YouTubeExtractor

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

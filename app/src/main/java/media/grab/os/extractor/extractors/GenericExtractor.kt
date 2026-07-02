package media.grab.os.extractor.extractors

import media.grab.os.data.model.MediaType
import media.grab.os.data.model.Platform
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

/** Fallback for any URL: direct media links handled inline, otherwise og:/twitter: meta. */
class GenericExtractor : Extractor {
    override val platform = Platform.GENERIC
    override fun canHandle(url: String) = true

    override fun extract(url: String): List<MediaInfo> {
        val direct = Regex(".*\\.(mp4|webm|mkv|mov|jpg|jpeg|png|gif|webp|mp3|m4a|aac|wav|ogg)(\\?.*)?$",
            RegexOption.IGNORE_CASE)
        if (direct.matches(url)) {
            val lower = url.lowercase()
            val type = when {
                Regex("\\.(mp4|webm|mkv|mov)").containsMatchIn(lower) -> MediaType.VIDEO
                Regex("\\.(mp3|m4a|aac|wav|ogg)").containsMatchIn(lower) -> MediaType.AUDIO
                else -> MediaType.IMAGE
            }
            return listOf(MediaInfo(url, type))
        }
        return MetaScraper.scrape(url, mobile = false)
    }
}

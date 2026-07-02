package media.grab.os.extractor.extractors

import media.grab.os.data.model.Platform
import media.grab.os.extractor.ExtractionException
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

/**
 * YouTube full-stream extraction requires signature de-ciphering (effectively yt-dlp).
 * To keep the build free of native binaries we grab the thumbnail/preview via og: tags
 * as a best-effort, and surface a clear message when only metadata is available.
 */
class YouTubeExtractor : Extractor {
    override val platform = Platform.YOUTUBE
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.YOUTUBE
    override fun extract(url: String): List<MediaInfo> =
        runCatching { MetaScraper.scrape(url, mobile = false) }
            .getOrElse {
                throw ExtractionException(
                    "YouTube video streams are protected. Only thumbnail/metadata could be retrieved."
                )
            }
}

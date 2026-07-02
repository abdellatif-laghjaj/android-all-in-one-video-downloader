package media.grab.os.extractor.extractors

import media.grab.os.data.model.Platform
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

class TikTokExtractor : Extractor {
    override val platform = Platform.TIKTOK
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.TIKTOK
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = true)
}

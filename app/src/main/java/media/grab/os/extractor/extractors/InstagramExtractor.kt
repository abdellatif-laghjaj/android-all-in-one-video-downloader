package media.grab.os.extractor.extractors

import media.grab.os.data.model.Platform
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

class InstagramExtractor : Extractor {
    override val platform = Platform.INSTAGRAM
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.INSTAGRAM
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = true)
}

package media.grab.os.extractor.extractors

import media.grab.os.data.model.Platform
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

class PinterestExtractor : Extractor {
    override val platform = Platform.PINTEREST
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.PINTEREST
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = false)
}

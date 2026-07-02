package media.grab.os.extractor.extractors

import media.grab.os.data.model.Platform
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

class TwitterExtractor : Extractor {
    override val platform = Platform.TWITTER
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.TWITTER
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = false)
}

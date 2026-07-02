package media.grab.os.extractor.extractors

import media.grab.os.data.model.Platform
import media.grab.os.extractor.Extractor
import media.grab.os.extractor.MediaInfo
import media.grab.os.extractor.MetaScraper

class FacebookExtractor : Extractor {
    override val platform = Platform.FACEBOOK
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.FACEBOOK
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = true)
}

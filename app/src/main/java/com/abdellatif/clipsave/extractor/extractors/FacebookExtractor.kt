package com.abdellatif.clipsave.extractor.extractors

import com.abdellatif.clipsave.data.model.Platform
import com.abdellatif.clipsave.extractor.Extractor
import com.abdellatif.clipsave.extractor.MediaInfo
import com.abdellatif.clipsave.extractor.MetaScraper

class FacebookExtractor : Extractor {
    override val platform = Platform.FACEBOOK
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.FACEBOOK
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = true)
}

package com.abdellatif.clipsave.extractor.extractors

import com.abdellatif.clipsave.data.model.Platform
import com.abdellatif.clipsave.extractor.Extractor
import com.abdellatif.clipsave.extractor.MediaInfo
import com.abdellatif.clipsave.extractor.MetaScraper

class TikTokExtractor : Extractor {
    override val platform = Platform.TIKTOK
    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.TIKTOK
    override fun extract(url: String): List<MediaInfo> = MetaScraper.scrape(url, mobile = true)
}

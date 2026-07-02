package media.grab.os.extractor

import media.grab.os.data.model.MediaType
import media.grab.os.network.HttpClient

/** Generic Open Graph / Twitter-card meta tag scraper, reused by most extractors. */
object MetaScraper {

    private fun meta(html: String, property: String): String? {
        val patterns = listOf(
            Regex("""<meta[^>]+(?:property|name)=["']$property["'][^>]+content=["']([^"']+)["']""", RegexOption.IGNORE_CASE),
            Regex("""<meta[^>]+content=["']([^"']+)["'][^>]+(?:property|name)=["']$property["']""", RegexOption.IGNORE_CASE)
        )
        for (p in patterns) {
            val m = p.find(html)?.groupValues?.getOrNull(1)
            if (!m.isNullOrBlank()) return decode(m)
        }
        return null
    }

    private fun decode(s: String): String = s
        .replace("&amp;", "&").replace("&quot;", "\"").replace("&#39;", "'")
        .replace("&lt;", "<").replace("&gt;", ">").replace("&#x2F;", "/").trim()

    fun scrape(url: String, mobile: Boolean = false): List<MediaInfo> {
        val html = HttpClient.getString(url, mobile) ?: throw ExtractionException("Could not load page.")
        return fromHtml(html)
    }

    fun fromHtml(html: String): List<MediaInfo> {
        val title = meta(html, "og:title") ?: meta(html, "twitter:title") ?: ""
        val thumb = meta(html, "og:image") ?: meta(html, "twitter:image")
        val video = meta(html, "og:video:secure_url")
            ?: meta(html, "og:video:url")
            ?: meta(html, "og:video")
            ?: meta(html, "twitter:player:stream")
        val image = meta(html, "og:image") ?: meta(html, "twitter:image")

        val results = mutableListOf<MediaInfo>()
        if (!video.isNullOrBlank()) {
            results += MediaInfo(video, MediaType.VIDEO, title, thumb)
        } else if (!image.isNullOrBlank()) {
            results += MediaInfo(image, MediaType.IMAGE, title, thumb)
        }
        if (results.isEmpty()) {
            throw ExtractionException("No media found in page metadata.")
        }
        return results
    }
}

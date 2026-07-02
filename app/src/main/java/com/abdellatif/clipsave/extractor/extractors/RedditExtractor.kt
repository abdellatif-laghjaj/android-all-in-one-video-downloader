package com.abdellatif.clipsave.extractor.extractors

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import com.abdellatif.clipsave.data.model.MediaType
import com.abdellatif.clipsave.data.model.Platform
import com.abdellatif.clipsave.extractor.ExtractionException
import com.abdellatif.clipsave.extractor.Extractor
import com.abdellatif.clipsave.extractor.MediaInfo
import com.abdellatif.clipsave.network.HttpClient

/** Reddit is the easiest real win: the public .json API exposes media directly. */
class RedditExtractor : Extractor {
    override val platform = Platform.REDDIT
    private val json = Json { ignoreUnknownKeys = true }

    override fun canHandle(url: String) = Platform.fromUrl(url) == Platform.REDDIT

    override fun extract(url: String): List<MediaInfo> {
        val clean = url.substringBefore("?").trimEnd('/')
        val jsonUrl = if (clean.endsWith(".json")) clean else "$clean.json"
        val body = HttpClient.getString(jsonUrl) ?: throw ExtractionException("Reddit unreachable.")
        val root = runCatching { json.parseToJsonElement(body) }.getOrNull()
            ?: throw ExtractionException("Invalid Reddit response.")

        val listing = (root as? JsonArray)?.firstOrNull()?.jsonObject
            ?: throw ExtractionException("Unexpected Reddit format.")
        val post = listing["data"]?.jsonObject
            ?.get("children")?.jsonArray?.firstOrNull()?.jsonObject
            ?.get("data")?.jsonObject
            ?: throw ExtractionException("No post data.")

        val title = post["title"]?.jsonPrimitive?.contentOrNull ?: "Reddit post"
        val results = mutableListOf<MediaInfo>()

        post["media"]?.jsonObject?.get("reddit_video")?.jsonObject
            ?.get("fallback_url")?.jsonPrimitive?.contentOrNull?.let {
                results += MediaInfo(it.substringBefore("?"), MediaType.VIDEO, title)
            }

        if (results.isEmpty()) {
            val direct = post["url_overridden_by_dest"]?.jsonPrimitive?.contentOrNull
                ?: post["url"]?.jsonPrimitive?.contentOrNull
            if (direct != null && direct.matches(Regex(".*\\.(jpg|jpeg|png|gif|webp|mp4)(\\?.*)?$", RegexOption.IGNORE_CASE))) {
                val type = if (direct.contains(".mp4", true)) MediaType.VIDEO else MediaType.IMAGE
                results += MediaInfo(direct, type, title)
            }
        }

        if (results.isEmpty()) {
            post["preview"]?.jsonObject?.get("images")?.jsonArray?.firstOrNull()
                ?.jsonObject?.get("source")?.jsonObject?.get("url")?.jsonPrimitive
                ?.contentOrNull?.let {
                    results += MediaInfo(it.replace("&amp;", "&"), MediaType.IMAGE, title)
                }
        }

        if (results.isEmpty()) throw ExtractionException("No downloadable media in this Reddit post.")
        return results
    }
}

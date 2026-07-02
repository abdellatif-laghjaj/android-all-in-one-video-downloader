package media.grab.os.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/** Shared OkHttp client with browser-like defaults. */
object HttpClient {

    const val DESKTOP_UA =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"

    const val MOBILE_UA =
        "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36"

    val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .build()

    fun request(url: String, mobile: Boolean = false): Request = Request.Builder()
        .url(url)
        .header("User-Agent", if (mobile) MOBILE_UA else DESKTOP_UA)
        .header("Accept", "*/*")
        .header("Accept-Language", "en-US,en;q=0.9")
        .build()

    fun getString(url: String, mobile: Boolean = false): String? =
        runCatching {
            client.newCall(request(url, mobile)).execute().use { resp ->
                if (resp.isSuccessful) resp.body?.string() else null
            }
        }.getOrNull()

    fun resolveFinalUrl(url: String): String =
        runCatching {
            client.newCall(request(url, mobile = true)).execute().use { it.request.url.toString() }
        }.getOrDefault(url)

    inline fun execute(url: String, mobile: Boolean = false, block: (Response) -> Unit) {
        client.newCall(request(url, mobile)).execute().use(block)
    }
}

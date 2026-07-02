package media.grab.os.extractor

import media.grab.os.data.model.MediaType

/** Result of an extraction: one downloadable media URL plus metadata. */
data class MediaInfo(
    val downloadUrl: String,
    val mediaType: MediaType,
    val title: String = "",
    val thumbnailUrl: String? = null,
    val suggestedExtension: String? = null
)

class ExtractionException(message: String) : Exception(message)

package com.example.composerestgallery.screens.gallery.api

import com.example.composerestgallery.screens.gallery.viewmodel.GalleryImage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializer(forClass = GalleryImage::class)
object GalleryImageSerializer : KSerializer<GalleryImage> {
    override fun deserialize(decoder: Decoder): GalleryImage {
        val jsonObject = decoder.decodeSerializableValue(JsonObject.serializer())
        val rawDescription = jsonObject.jsonObject["description"]?.jsonPrimitive?.content
        val description = rawDescription?.let { if (it == "null" || it.isBlank()) null else it }
        val userName = jsonObject.jsonObject["user"]?.jsonObject?.get("name")?.jsonPrimitive?.content
        val url = jsonObject.jsonObject["urls"]?.jsonObject?.get("regular")?.jsonPrimitive?.content

        if (userName != null && url != null) {
            return GalleryImage(
                url = url, description = description, userName = userName
            )
        } else {
            throw SerializationException("Username or url is empty")
        }
    }

    override fun serialize(encoder: Encoder, value: GalleryImage) {
        TODO("Not yet implemented, not needed")
    }
}
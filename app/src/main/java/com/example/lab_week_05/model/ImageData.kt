package com.example.lab_week_05.model
import android.R.attr.name
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageData(
    @field:Json(name = "url") val imageUrl: String,
    @field:Json(name= "id") val id: String,
    @field:Json(name = "breeds") val breeds: List<CatBreedData>?
)
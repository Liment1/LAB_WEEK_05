package com.example.lab_week_05.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class CatBreedData(
    @field:Json(name = "name")val name: String,
    @field:Json(name = "temperament")val temperament: String

)
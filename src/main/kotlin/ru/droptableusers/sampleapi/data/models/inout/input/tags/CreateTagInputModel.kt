package ru.droptableusers.sampleapi.data.models.inout.input.tags

import kotlinx.serialization.Serializable

@Serializable
data class CreateTagInputModel(
    val tagText: String,
)

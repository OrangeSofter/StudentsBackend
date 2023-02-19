package ru.orange.studback.dataservices.courses.data

import kotlinx.serialization.Serializable

@Serializable
data class CourseRequestDto(
    val name: String,
    val description: String,
)
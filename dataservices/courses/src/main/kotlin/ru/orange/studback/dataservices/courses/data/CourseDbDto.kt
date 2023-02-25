package ru.orange.studback.dataservices.courses.data

import kotlinx.serialization.Serializable

@Serializable
data class CourseDbDto(
    val description: String,
)
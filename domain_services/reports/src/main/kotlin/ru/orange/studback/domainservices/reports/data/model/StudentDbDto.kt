package ru.orange.studback.domainservices.reports.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseDbDto(
    val description: String,
)
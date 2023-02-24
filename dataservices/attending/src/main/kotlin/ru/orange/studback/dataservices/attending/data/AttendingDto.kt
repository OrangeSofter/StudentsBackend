package ru.orange.studback.dataservices.attending.data

import kotlinx.serialization.Serializable

@Serializable
data class AttendingDto(
    val learningWeek: Int,
    val courseName: String,
    val attendingStudentNums: List<String>,
)
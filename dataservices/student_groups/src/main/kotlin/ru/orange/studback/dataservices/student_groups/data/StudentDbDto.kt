package ru.orange.studback.dataservices.student_groups.data

import kotlinx.serialization.Serializable

@Serializable
data class StudentDbDto(
    val fio: String,
    val isMale: Boolean,
    val birthDate: String,
    val phone: String
)
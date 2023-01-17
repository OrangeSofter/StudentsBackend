package ru.orange.studback.dataservices.students.data

import kotlinx.serialization.Serializable

@Serializable
data class StudentDbDto(
    val fio: String,
    val isMale: Boolean,
    val birthDate: String,
    val phone: String
)
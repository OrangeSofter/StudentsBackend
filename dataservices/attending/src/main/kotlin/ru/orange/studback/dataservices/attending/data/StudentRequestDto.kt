package ru.orange.studback.dataservices.attending.data

import kotlinx.serialization.Serializable

@Serializable
data class StudentRequestDto(
    val number: String,
    val fio: String,
    val isMale: Boolean,
    val birthDate: String,
    val phone: String
)
package ru.orange.studback.dataservices.student_groups.data

import kotlinx.serialization.Serializable

@Serializable
data class GroupRequestDto(
    val groupName: String,
    val educationBeginDate: String,
    val studentNumbers: List<String>,
)
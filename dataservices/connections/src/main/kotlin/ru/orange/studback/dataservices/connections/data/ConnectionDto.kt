package ru.orange.studback.dataservices.connections.data

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionDto(
    val courseName: String,
    val groupName: String,
    val studentNumber: String,
)
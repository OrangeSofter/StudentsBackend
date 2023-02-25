package ru.orange.studback.domainservices.reports.data.model.response

import kotlinx.serialization.Serializable


@Serializable
data class StudentAttendingResponseModel(
    val startWeek: Int,
    val endWeek: Int,
    val courseNamePart: String?,
    val studentsInfo: List<StudentFullInfo>
)

@Serializable
data class StudentFullInfo(
    val number: String,
    val fio: String,
    val courseName: String,
    val courseDescription: String,
    val groupName: String,
    val groupEducationBeginDate: String,
    val attendingPercent: Int,
)
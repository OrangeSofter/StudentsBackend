package ru.orange.studback.domainservices.reports.domain.model

data class CourseConnection(
    val courseName: String,
    val groups: List<GroupConnection>
)

data class GroupConnection(
    val groupName: String,
    val studentsNumbers: List<String>
)

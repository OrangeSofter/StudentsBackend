package ru.orange.studback.domainservices.reports.data.repository

import ru.orange.studback.domainservices.reports.domain.model.Course

class CoursesRepository {

    suspend fun getCourse(courseName: String): Course {
        TODO()
    }
}
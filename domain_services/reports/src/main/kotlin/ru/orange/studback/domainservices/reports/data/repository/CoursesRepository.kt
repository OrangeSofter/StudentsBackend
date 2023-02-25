package ru.orange.studback.domainservices.reports.data.repository

import ru.orange.studback.domainservices.reports.domain.model.Course

class CoursesRepository {

    /**
     * Возвращает список курсов
     * @param [courseNamePart] часть имени курса, которая должна содержаться в ответе,
     * если null - то вернет все курсы
     */
    suspend fun getCourses(courseNamePart: String? = null): List<Course> {

    }
}
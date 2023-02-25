package ru.orange.studback.domainservices.reports.data.repository

import ru.orange.studback.domainservices.reports.domain.model.Attending

class AttendingRepository {
    suspend fun getAttending(
        startWeek: Int = 1,
        endWeek: Int = 16,
        courseNamePart: String? = null
    ): List<Attending> {

    }
}
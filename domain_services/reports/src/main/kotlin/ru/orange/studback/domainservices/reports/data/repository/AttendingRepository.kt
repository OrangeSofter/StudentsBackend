package ru.orange.studback.domainservices.reports.data.repository

import ru.orange.studback.domainservices.reports.domain.model.Attending

class AttendingRepository {

    suspend fun getAttending(
        startWeek: Int,
        endWeek: Int,
        courseNamePart: String? = null
    ): List<Attending> {
        TODO()
    }
}
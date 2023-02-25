package ru.orange.studback.domainservices.reports.domain

import io.ktor.server.application.*
import io.ktor.server.request.*
import ru.orange.studback.common.extensions.respondBadRequest
import ru.orange.studback.common.extensions.respondInternalFailure
import ru.orange.studback.common.extensions.respondJson
import ru.orange.studback.common.extensions.respondSuccess
import ru.orange.studback.domainservices.reports.data.model.CourseDbDto
import ru.orange.studback.domainservices.reports.data.repository.*
import ru.orange.studback.domainservices.reports.domain.model.Attending
import ru.orange.studback.domainservices.reports.domain.model.Course
import ru.orange.studback.domainservices.reports.domain.model.Student


internal class Controller(
    private val attendingRepository: AttendingRepository,
    private val connectionsRepository: ConnectionsRepository,
    private val coursesRepository: CoursesRepository,
    private val studentGroupsRepository: StudentGroupsRepository,
    private val studentsRepository: StudentsRepository,
) {

    suspend fun getLowAttendingStudents(call: ApplicationCall) {
        val courseNamePart = call.request.queryParameters["course_name_part"]
        val intervalStart = call.request.queryParameters["interval_start"]?.toIntOrNull()
        val intervalEnd = call.request.queryParameters["interval_end"]?.toIntOrNull()
        val reportStudentsCount = 10

        if (intervalStart == null || intervalEnd == null) {
            call.respondBadRequest()
            return
        }
        val students: List<Student> = studentsRepository.getStudents()
        val courses = coursesRepository.getCourses(courseNamePart)
        val attendingList = attendingRepository
            .getAttending(intervalStart, intervalEnd, courseNamePart)
            .sortedBy { it.attendingPercent }
        val zeroAttendingStudents = students.filter { student ->
            val studentAttending = attendingList.find { attending ->
                student.number == attending.studentNumber
            }
            studentAttending == null
        }
        val zeroAttendingList = zeroAttendingStudents.map { Attending(it.number, 0) }
        val fullAttendingList: List<Attending> = zeroAttendingList + attendingList
        val reportAttending = fullAttendingList.take(reportStudentsCount)


        call.respondJson(result.getOrNull())
    }

    suspend fun get(call: ApplicationCall) {


        val result = runCatching {


        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondJson(result.getOrNull())
    }

    suspend fun remove(call: ApplicationCall) {
        val num = call.receive<String>()
        val result = runCatching {

        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }
}

private fun Course.toDbDto() = CourseDbDto(description)
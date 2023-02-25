package ru.orange.studback.domainservices.reports.domain

import io.ktor.server.application.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.orange.studback.common.extensions.respondBadRequest
import ru.orange.studback.common.extensions.respondJson
import ru.orange.studback.domainservices.reports.data.model.response.StudentAttendingResponseModel
import ru.orange.studback.domainservices.reports.data.model.response.StudentFullInfo
import ru.orange.studback.domainservices.reports.data.repository.*
import ru.orange.studback.domainservices.reports.domain.mapper.mapToFullInfo


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

        val attendingList = attendingRepository.getAttending(intervalStart, intervalEnd, courseNamePart)
        val lowAttendingList = attendingList.sortedBy { it.attendingPercent }.take(reportStudentsCount)


        val fullInfoList = mutableListOf<StudentFullInfo>()

        lowAttendingList.forEach { attending ->
            val studentInfo = studentsRepository.getStudent(attending.studentNumber)
            val groupInfo = studentGroupsRepository.getGroup(attending.studentNumber)
            val courseName = connectionsRepository.getCourseName(groupInfo.groupName)
            val courseInfo = coursesRepository.getCourse(courseName)

            fullInfoList.add(mapToFullInfo(attending, studentInfo, courseInfo, groupInfo))
        }
        val responseModel = StudentAttendingResponseModel(
            intervalStart,
            intervalEnd,
            courseNamePart,
            fullInfoList
        )
        call.respondJson(Json.encodeToString(responseModel))
    }

}

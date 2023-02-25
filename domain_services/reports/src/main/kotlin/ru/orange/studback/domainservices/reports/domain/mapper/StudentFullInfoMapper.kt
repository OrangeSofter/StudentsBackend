package ru.orange.studback.domainservices.reports.domain.mapper

import ru.orange.studback.domainservices.reports.data.model.response.StudentFullInfo
import ru.orange.studback.domainservices.reports.domain.model.Attending
import ru.orange.studback.domainservices.reports.domain.model.Course
import ru.orange.studback.domainservices.reports.domain.model.Group
import ru.orange.studback.domainservices.reports.domain.model.Student

fun mapToFullInfo(attending: Attending, studentInfo: Student, course: Course, group: Group) =
    StudentFullInfo(
        number = attending.studentNumber,
        fio = studentInfo.fio,
        courseName = course.name,
        courseDescription = course.description,
        groupName = group.groupName,
        groupEducationBeginDate = group.educationBeginDate,
        attendingPercent = attending.attendingPercent
    )
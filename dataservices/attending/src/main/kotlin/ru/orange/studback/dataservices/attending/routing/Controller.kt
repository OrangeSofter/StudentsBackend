package ru.orange.studback.dataservices.attending.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.orange.studback.common.extensions.*
import ru.orange.studback.dataservices.attending.data.AttendingDto
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

internal class Controller(private val postgreConnection: Connection) {


    /**
     *  CREATE TABLE IF NOT EXISTS $TABLE_NAME (
     *                     $ID_COLUMN                      serial primary key,
     *                     $LEARNING_WEEK_COLUMN           integer,
     *                     $COURSE_NAME_COLUMN             text,
     *                     $ATTENDING_STUDENT_NUMS_COLUMN  text[],
     *                     $ABSENT_STUDENT_NUMS_COLUMN     text[]
     *                 );
     */
    suspend fun put(call: ApplicationCall) {
        val requestModel = call.receive<AttendingDto>()
        val result = runCatching {
            val st: Statement = postgreConnection.createStatement()
            st.executeUpdate(
                """
                 INSERT INTO $TABLE_NAME VALUES (
                 DEFAULT,
                 ${requestModel.learningWeek}, 
                 '${requestModel.courseName}', 
                 '{${requestModel.attendingStudentNums.joinToString { "\"$it\"" }}}', 
                 '{${requestModel.absentStudentNums.joinToString { "\"$it\"" }}}'
                )
                """.trimIndent()
            )
            st.close()
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }

    suspend fun getAttendingStudents(call: ApplicationCall) {
        val learningWeek = call.request.queryParameters["learningWeek"]?.toIntOrNull()
        val courseName = call.request.queryParameters["courseName"]
        if (learningWeek == null || courseName == null) {
            call.respondBadRequest()
            return
        }
        val result = runCatching<Array<String>?> {
            val st: Statement = postgreConnection.createStatement()
            val rs: ResultSet = st.executeQuery(
                """
                SELECT $ATTENDING_STUDENT_NUMS_COLUMN FROM $TABLE_NAME 
                WHERE $LEARNING_WEEK_COLUMN = $learningWeek 
                AND $COURSE_NAME_COLUMN = '$courseName'
                """.trimIndent()
            )
            if (!rs.next()) {
                call.respondNoContent()
                return
            }
            val arr: Array<String>? = rs.getArray(1)?.array as Array<String>?
            rs.close()
            st.close()
            arr
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        val arr = result.getOrNull()
        if (arr.isNullOrEmpty()) {
            call.respondNoContent()
            return
        }
        call.respondJson(Json.encodeToString(arr))
    }

    suspend fun update(call: ApplicationCall) {
        val requestModel = call.receive<AttendingDto>()
        val result = runCatching {
            val st: Statement = postgreConnection.createStatement()
            st.executeUpdate(
                """
                UPDATE $TABLE_NAME SET
                ($LEARNING_WEEK_COLUMN, $ATTENDING_STUDENT_NUMS_COLUMN, $ABSENT_STUDENT_NUMS_COLUMN) = (
                 ${requestModel.learningWeek},
                 '{${requestModel.attendingStudentNums.joinToString()}',
                 '{${requestModel.absentStudentNums.joinToString()}'
                ) WHERE $COURSE_NAME_COLUMN = ${requestModel.courseName}
                """.trimIndent()
            )
            st.close()
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }

    suspend fun removeLecture(call: ApplicationCall) {
        val courseName = call.receive<String>()
        val result = runCatching {
            val st: Statement = postgreConnection.createStatement()
            st.executeUpdate(
                """
                DELETE FROM $TABLE_NAME 
                WHERE $COURSE_NAME_COLUMN = $courseName
                """.trimIndent()
            )
            st.close()
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }
}

private const val TABLE_NAME = "attending"

private const val ID_COLUMN = "id"
private const val LEARNING_WEEK_COLUMN = "learning_week"
private const val COURSE_NAME_COLUMN = "course_name"
private const val ATTENDING_STUDENT_NUMS_COLUMN = "attending_student_nums"
private const val ABSENT_STUDENT_NUMS_COLUMN = "absent_student_nums"
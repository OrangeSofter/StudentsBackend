package ru.orange.studback.dataservices.courses.routing

import com.jillesvangurp.ktsearch.SearchClient
import com.jillesvangurp.ktsearch.bulk
import com.jillesvangurp.ktsearch.deleteDocument
import com.jillesvangurp.ktsearch.search
import com.jillesvangurp.searchdsls.querydsl.bool
import com.jillesvangurp.searchdsls.querydsl.ids
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.orange.studback.common.extensions.respondBadRequest
import ru.orange.studback.common.extensions.respondInternalFailure
import ru.orange.studback.common.extensions.respondJson
import ru.orange.studback.common.extensions.respondSuccess
import ru.orange.studback.dataservices.courses.data.CourseDbDto
import ru.orange.studback.dataservices.courses.data.CourseRequestDto


internal class Controller(private val elastic: SearchClient) {

    suspend fun put(call: ApplicationCall) {
        val requestModel = call.receive<CourseRequestDto>()
        val dbModel = requestModel.toDbDto()
        val result = runCatching {
            elastic.bulk {
                index(
                    source = Json.encodeToString(dbModel),
                    index = INDEX_NAME,
                    id = requestModel.name
                )
            }
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }

    suspend fun get(call: ApplicationCall) {
        val courseName = call.request.queryParameters["course_name"]
        if (courseName == null) {
            call.respondBadRequest()
            return
        }

        val result = runCatching {
            elastic.search(INDEX_NAME) {
                query = bool {
                    must(
                        ids(courseName)
                    )
                }
            }
                .hits?.hits?.firstOrNull()?.source?.toString()

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
            elastic.deleteDocument(INDEX_NAME, num)
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }
}

private fun CourseRequestDto.toDbDto() = CourseDbDto(description)

private const val INDEX_NAME = "courses"
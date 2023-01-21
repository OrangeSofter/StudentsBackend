package ru.orange.studback.dataservices.connections.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import redis.clients.jedis.JedisPool
import ru.orange.studback.common.extensions.*
import ru.orange.studback.dataservices.connections.data.StudentDbDto
import ru.orange.studback.dataservices.connections.data.StudentRequestDto

internal class Controller(private val jedisPool: JedisPool) {

    suspend fun putStudent(call: ApplicationCall) {
        val requestModel = call.receive<StudentRequestDto>()
        val dbModel = requestModel.toDbDto()
        val dbString = Json.encodeToString(dbModel)
        runCatching {
            jedisPool.resource.use { jedis ->
                jedis.set(requestModel.number, dbString)
            }
            call.respondSuccess()
        }.onFailure {
            call.respondInternalFailure(it)
        }
    }

    suspend fun getStudent(call: ApplicationCall) {
        val num = call.request.queryParameters["number"]
        if (num == null) {
            call.respondBadRequest()
            return
        }
        val result = runCatching<String?> {
            jedisPool.resource.use { jedis ->
                jedis.get(num)
            }
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        val dbString = result.getOrNull()
        if (dbString == null) {
            call.respondNoContent()
            return
        }
        call.respondJson(dbString)
    }

    suspend fun removeStudent(call: ApplicationCall) {
        val num = call.receive<String>()
        val result = runCatching {
            jedisPool.resource.use { jedis ->
                jedis.del(num)
            }
        }
        if (result.isFailure) {
            call.respondInternalFailure(result.exceptionOrNull())
            return
        }
        call.respondSuccess()
    }
}

private fun StudentRequestDto.toDbDto(): StudentDbDto = StudentDbDto(
    fio = fio,
    isMale = isMale,
    birthDate = birthDate,
    phone = phone,
)
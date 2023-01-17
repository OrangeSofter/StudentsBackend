package ru.orange.studback.dataservices.students.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import redis.clients.jedis.JedisPool
import ru.orange.studback.common.extensions.respondFailure

internal class Controller(private val jedisPool: JedisPool) {

    suspend fun putStudents(call: ApplicationCall, ){
        runCatching {
            jedisPool.resource.use { jedis ->
                val cl = jedis.get("clientName") ?: "notSet"
                call.respondText (cl)
            }
        }.onFailure {
            call.respondFailure(it)
        }
    }

    suspend fun getStudents(call: ApplicationCall){
        runCatching {
            jedisPool.resource.use { jedis ->
                jedis.set("clientName", "MyClient")
            }
        }.onFailure {
            call.respondFailure(it)
        }
        call.respondText("Success")
    }
}
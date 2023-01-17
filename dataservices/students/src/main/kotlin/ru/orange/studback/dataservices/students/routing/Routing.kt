package ru.orange.studback.dataservices.students.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import redis.clients.jedis.JedisPool


fun Application.configureRouting() {
    val pool = JedisPool("localhost", 6379)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/put_student") {
            runCatching {
                pool.resource.use { jedis ->
                    jedis.set("clientName", "MyClient")
                }
            }.onFailure {
                call.respondText { "Failure" }
            }
            call.respondText("Success")
        }
        get("/get_students"){
            runCatching {
                pool.resource.use { jedis ->
                    val cl = jedis.get("clientName") ?: "notSet"
                    call.respondText (cl)
                }
            }.onFailure {
                call.respondText { "Failure" }
            }
        }
    }
}

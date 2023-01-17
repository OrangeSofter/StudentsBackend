package ru.orange.studback.dataservices.students.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import redis.clients.jedis.JedisPool


fun Application.configureRouting() {
    val pool = JedisPool("localhost", 6379)
    val controller = Controller(pool)

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/put_student") { controller.putStudents(call) }
        get("/get_students"){controller.getStudents(call)}
    }
}

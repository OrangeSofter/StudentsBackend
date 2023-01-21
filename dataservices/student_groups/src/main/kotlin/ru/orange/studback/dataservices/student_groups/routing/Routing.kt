package ru.orange.studback.dataservices.student_groups.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import redis.clients.jedis.JedisPool


fun Application.configureRouting() {
    val pool = JedisPool("localhost", 6379)
    val controller = Controller(pool)

    routing {
        get("/") {
            call.respondText("Ok")
        }
        post("/put_student") { controller.putStudent(call) }
        get("/get_student") { controller.getStudent(call) }
        post("/remove_student") { controller.removeStudent(call) }
    }
}

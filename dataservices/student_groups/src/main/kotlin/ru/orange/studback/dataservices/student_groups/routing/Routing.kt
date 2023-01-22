package ru.orange.studback.dataservices.student_groups.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    val controller = Controller()
    routing {
        get("/") { call.respondText("Ok") }
        post("/put_group") { controller.putGroup(call) }
        get("/get_group") { controller.getGroup(call) }
        post("/update_group") { controller.updateGroup(call) }
        delete("/remove_group") { controller.removeGroup(call) }
    }
}

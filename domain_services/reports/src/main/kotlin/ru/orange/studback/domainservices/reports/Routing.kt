package ru.orange.studback.domainservices.reports


import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.orange.studback.domainservices.reports.domain.Controller


internal fun Application.configureRouting(controller: Controller) {
    routing {
        get("/") {
            call.respondText("Ok")
        }
        post("/put") { controller.put(call) }
        get("/get") { controller.get(call) }
        post("/remove") { controller.remove(call) }
    }
}

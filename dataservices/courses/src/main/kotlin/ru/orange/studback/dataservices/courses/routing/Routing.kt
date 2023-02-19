package ru.orange.studback.dataservices.courses.routing


import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.SearchClient
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    val client = SearchClient(
        KtorRestClient()
    )
    val controller = Controller(client)
    routing {
        get("/") {
            call.respondText("Ok")
        }
        post("/put") { controller.put(call) }
        get("/get") { controller.get(call) }
        post("/remove") { controller.remove(call) }
    }
}

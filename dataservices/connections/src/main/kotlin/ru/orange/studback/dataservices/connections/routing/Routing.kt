package ru.orange.studback.dataservices.connections.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase


fun Application.configureRouting() {
    val pool = GraphDatabase.driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "myNeoPass"))
    val controller = Controller(pool)

    routing {
        get("/") {
            call.respondText("Ok")
        }
        post("/put_connection") { controller.putConnection(call) }
        get("/get_connection") { controller.getConnection(call) }
        post("/remove_connection") { controller.removeConnection(call) }
    }
}

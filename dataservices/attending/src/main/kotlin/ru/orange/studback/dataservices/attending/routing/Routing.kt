package ru.orange.studback.dataservices.attending.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager
import java.util.*


fun Application.configureRouting() {
    val url = "jdbc:postgresql://localhost/postgres"
    val props = Properties()
    props.setProperty("user", "postgres")
    props.setProperty("password", "password")
    props.setProperty("ssl", "false")
    val conn = DriverManager.getConnection(url, props)
    val controller = Controller(conn)

    routing {
        get("/") {
            call.respondText("Ok")
        }
        post("/put") { controller.put(call) }
        get("/get") { controller.getAttendingStudents(call) }
        post("/update") { controller.put(call) }
        post("/remove_lecture") { controller.removeLecture(call) }
    }
}

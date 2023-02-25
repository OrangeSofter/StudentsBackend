package ru.orange.studback.domainservices.reports

import com.jillesvangurp.ktsearch.KtorRestClient
import com.jillesvangurp.ktsearch.SearchClient
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import ru.orange.studback.domainservices.reports.domain.Controller

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

internal fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    val client = SearchClient(
        KtorRestClient()
    )
    val controller = Controller(client)
    configureRouting(controller)
}

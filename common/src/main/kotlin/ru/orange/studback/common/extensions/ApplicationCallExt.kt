package ru.orange.studback.common.extensions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondInternalFailure(exception: Throwable? = null) {
    respond(HttpStatusCode.InternalServerError, exception.toString())
}

suspend fun ApplicationCall.respondBadRequest(exception: Throwable? = null) {
    respond(HttpStatusCode.BadRequest, exception.toString())
}

suspend fun ApplicationCall.respondSuccess() {
    respond(HttpStatusCode.OK, "Success")
}

suspend fun ApplicationCall.respondNoContent() {
    respond(HttpStatusCode.NoContent, "NoContent")
}

suspend fun ApplicationCall.respondJson(json: String) {
    response.header(HttpHeaders.ContentType, "application/json")
    respond(HttpStatusCode.OK, json)
}
package ru.orange.studback.common.extensions

import io.ktor.http.*
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.*

suspend fun ApplicationCall.respondFailure(exception: Throwable){
    respond(HttpStatusCode.InternalServerError, exception.toString())
}
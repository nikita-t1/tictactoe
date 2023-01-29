package dev.nikitatarasov

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import dev.nikitatarasov.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSockets()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureCORS()
}

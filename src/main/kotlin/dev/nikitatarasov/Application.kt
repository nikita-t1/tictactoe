package dev.nikitatarasov

import dev.nikitatarasov.plugins.configureCORS
import dev.nikitatarasov.plugins.configureDevMode
import dev.nikitatarasov.plugins.configureMonitoring
import dev.nikitatarasov.plugins.configureRouting
import dev.nikitatarasov.plugins.configureSecurity
import dev.nikitatarasov.plugins.configureSerialization
import dev.nikitatarasov.plugins.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDevMode()
    configureSockets()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
    configureCORS()
}


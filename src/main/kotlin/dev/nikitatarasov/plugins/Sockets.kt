package dev.nikitatarasov.plugins

import dev.nikitatarasov.wrapper.DefaultWebSocketServerSessionWrapper
import dev.nikitatarasov.startSession
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import kotlinx.serialization.json.Json
import java.time.Duration

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(
            Json {
                encodeDefaults = true
                prettyPrint = true
                isLenient = true
            }
        )
    }

    routing {
        webSocket("/ws") {
            val sessionWrapper = DefaultWebSocketServerSessionWrapper(this)
            startSession(sessionWrapper)
        }
    }
}


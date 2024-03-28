package dev.nikitatarasov.wrapper

import io.ktor.server.application.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ReceiveChannel

interface WebSocketSessionWrapper {

    val call: ApplicationCall
    suspend fun send(message: String)
    suspend fun sendSerialized(message: Any?)
    suspend fun close()
    val incoming: ReceiveChannel<Frame>
}

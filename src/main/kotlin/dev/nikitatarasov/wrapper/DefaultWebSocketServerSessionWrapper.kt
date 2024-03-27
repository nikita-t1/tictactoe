package dev.nikitatarasov.wrapper

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ReceiveChannel


class DefaultWebSocketServerSessionWrapper(private val session: DefaultWebSocketServerSession) : WebSocketSessionWrapper {

    override val call: ApplicationCall get() = session.call
    override suspend fun send(message: String) = session.send(message)
    override val incoming: ReceiveChannel<Frame> get() = session.incoming
    override suspend fun sendSerialized(message: Any?) = session.sendSerialized(message)
    override suspend fun close() = session.close()

}

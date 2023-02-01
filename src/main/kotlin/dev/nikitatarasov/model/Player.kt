package dev.nikitatarasov.model

import io.ktor.server.websocket.*
import io.ktor.websocket.DefaultWebSocketSession
import java.util.*

data class Player(val id: UUID = UUID.randomUUID(), val symbol: PlayerSymbol, val session: DefaultWebSocketServerSession) {

}

enum class PlayerSymbol {
    NOUGHT,
    CROSS,
    UNSET;

    fun fieldToSymbol(): String {
        return when (this) {
            NOUGHT -> "O"
            CROSS -> "X"
            UNSET -> " "
        }
    }
}

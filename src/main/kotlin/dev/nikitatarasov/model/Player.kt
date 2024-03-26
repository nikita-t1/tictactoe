package dev.nikitatarasov.model

import dev.nikitatarasov.serializer.UUIDSerializer
import io.ktor.server.websocket.DefaultWebSocketServerSession
import kotlinx.coroutines.isActive
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

/**
 * Represents a player in the Tic-Tac-Toe game.
 *
 * @property symbol The symbol assigned to the player.
 * @property session The WebSocket session associated with the player. Can be null if the player is not connected.
 */
@Serializable
data class Player(
    val symbol: PlayerSymbol,
) {
    @Transient var session: DefaultWebSocketServerSession? = null

    fun isConnected(): Boolean = session != null //&& session!!.isActive
}

@Serializable
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

    fun fieldToInt(): Int{
        return when (this) {
            NOUGHT -> 2
            CROSS -> 1
            UNSET -> 0
        }
    }
}

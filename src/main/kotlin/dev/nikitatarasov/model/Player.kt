package dev.nikitatarasov.model

import dev.nikitatarasov.serializer.UUIDSerializer
import io.ktor.server.websocket.*
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Player(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    val symbol: PlayerSymbol,
//    val session: DefaultWebSocketServerSession
) {
    var session: DefaultWebSocketServerSession? = null

    fun isReady(): Boolean = session != null
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
}

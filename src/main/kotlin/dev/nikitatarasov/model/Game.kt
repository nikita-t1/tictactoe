package dev.nikitatarasov.model

import dev.nikitatarasov.util.GameBoardUtils
import dev.nikitatarasov.util.now
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlinx.datetime.LocalDateTime.Companion as LocalDateTime
import io.ktor.server.websocket.DefaultWebSocketServerSession

@Serializable
data class Game(
    val id: String = randomGameId(),
    val firstPlayer: Player = Player(symbol = PlayerSymbol.CROSS),
    val secondPlayer: Player = Player(symbol = PlayerSymbol.NOUGHT),
) {
    val gameBoard: GameBoard = GameBoard()
    var awaitMoveByPlayer: Player = chooseRandomPlayer() // Randomly select who goes first
    val creationTime = LocalDateTime.now()
    var rematchRequested: Player? = null

    private fun chooseRandomPlayer(): Player {
        return if (Random.nextBoolean()) firstPlayer else secondPlayer
    }

    fun bothPlayersConnected(): Boolean {
        return firstPlayer.isConnected() && secondPlayer.isConnected()
    }

    fun hasGameWinner(): Player? {
        val symbol: PlayerSymbol = GameBoardUtils.checkWinner(gameBoard) ?: return null
        return when (symbol) {
            firstPlayer.symbol -> firstPlayer
            secondPlayer.symbol -> secondPlayer
            else -> null
        }
    }

    fun removePlayer(session: DefaultWebSocketServerSession) {
        val player = getPlayerBySession(session)
        player?.session = null
    }

    fun getPlayerBySession(session: DefaultWebSocketServerSession): Player? {
        return when (session) {
            firstPlayer.session -> firstPlayer
            secondPlayer.session -> secondPlayer
            else -> null
        }
    }

    fun getOpponent(player: Player): Player {
        return when (player) {
            firstPlayer -> secondPlayer
            secondPlayer -> firstPlayer
            else -> throw IllegalArgumentException("Player not found")
        }
    }

    companion object {
        private val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        fun randomGameId(): String {
            return (1..4)
                .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
                .joinToString("")
        }
    }

}




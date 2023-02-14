package dev.nikitatarasov.model

import dev.nikitatarasov.util.now
import io.ktor.server.websocket.*
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlinx.datetime.LocalDateTime.Companion as LocalDateTime

data class Game(
    val id: String = randomGameId(),
    val firstPlayer: Player = Player(symbol = PlayerSymbol.CROSS),
    var secondPlayer: Player = Player(symbol = PlayerSymbol.NOUGHT),
) {
    val gameBoard: GameBoard = GameBoard()
    var awaitMoveByPlayer: Player = firstPlayer
    val players = listOf(firstPlayer, secondPlayer)
    val creationTime = LocalDateTime.now()

    fun hasGameWinner(): Player? {
        return gameBoard.hasGameWinner(firstPlayer!!, secondPlayer!!)
    }

    fun removePlayer(session: DefaultWebSocketServerSession) {
        val player = getPlayerBySession(session)
        player?.session = null
    }

    fun getPlayerBySession(session: DefaultWebSocketServerSession): Player? {
        return if (firstPlayer.session == session) {
            firstPlayer
        } else if (secondPlayer.session == session) {
            secondPlayer
        } else {
            null
        }
    }

    fun getOpponent(player: Player): Player {
        return if (player == firstPlayer) {
            secondPlayer
        } else {
            firstPlayer
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




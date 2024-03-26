package dev.nikitatarasov.model

import dev.nikitatarasov.util.now
import io.ktor.server.websocket.*
import kotlinx.serialization.Serializable
import kotlin.random.Random
import kotlinx.datetime.LocalDateTime.Companion as LocalDateTime

/**
 * Represents a Tic Tac Toe game.
 *
 * @property id The unique ID of the game.
 * @property firstPlayer The first player of the game.
 * @property secondPlayer The second player of the game.
 * @property gameBoard The game board of the game.
 * @property awaitMoveByPlayer The player whose move is awaited.
 * @property creationTime The time when the game was created.
 * @property rematchRequested The players who requested or accepted a rematch.
 */
@Serializable
data class Game(
    val id: String,
    val firstPlayer: Player = Player(symbol = PlayerSymbol.CROSS),
    val secondPlayer: Player = Player(symbol = PlayerSymbol.NOUGHT),
) {
    val gameBoard: GameBoard = GameBoard()
    var awaitMoveByPlayer: Player = chooseRandomPlayer() // Randomly select who goes first
    val creationTime = LocalDateTime.now()
    var rematchRequested: MutableSet<Player> = mutableSetOf()

    private fun chooseRandomPlayer(): Player {
        return if (Random.nextBoolean()) firstPlayer else secondPlayer
    }

    fun bothPlayersConnected(): Boolean {
        return firstPlayer.isConnected() && secondPlayer.isConnected()
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

    fun getPlayerBySymbol(symbol: Int): Player {
        return when (symbol) {
            firstPlayer.symbol.fieldToInt() -> firstPlayer
            secondPlayer.symbol.fieldToInt() -> secondPlayer
            else -> throw IllegalArgumentException("Player not found")
        }
    }

    fun getOpponent(player: Player): Player {
        return when (player) {
            firstPlayer -> secondPlayer
            secondPlayer -> firstPlayer
            else -> throw IllegalArgumentException("Player not found")
        }
    }

}




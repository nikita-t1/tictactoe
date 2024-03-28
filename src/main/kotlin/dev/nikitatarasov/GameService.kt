package dev.nikitatarasov

import dev.nikitatarasov.exceptions.GameLobbyIsFullException
import dev.nikitatarasov.exceptions.TimeoutSecondPlayerException
import dev.nikitatarasov.model.Game
import dev.nikitatarasov.model.Player
import dev.nikitatarasov.wrapper.WebSocketSessionWrapper
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * GameService class is responsible for handling game-related operations.
 */
object GameService {

    /**
     * Represents the timeout duration in milliseconds for awaiting a player's connection.
     *
     * The timeout duration is set to 5 minutes. If a player does not connect within this duration, the game will be closed/removed.
     */
    const val AWAIT_PLAYER_TIMEOUT_MS = 1000 * 60 * 5 // 5 min

    /**
     * Represents a pool of characters used for generating random game IDs.
     * The pool contains uppercase letters (A-Z) and digits (0-9).
     */
    private val charPool: List<Char> = ('A'..'Z') + ('0'..'9')

    fun findGame(gameCode: String): Game? {
        return GameStorage.findGame(gameCode)
    }

    fun generateGameCode(): String {
        var gameCode: String
        do {
            gameCode = randomGameId()
        } while (findGame(gameCode) != null)
        return gameCode
    }

    fun findOrCreateGame(gameCode: String): Game {
        return GameStorage.findOrCreateGame(gameCode)
    }

    suspend fun connectPlayerToGame(game: Game, session: WebSocketSessionWrapper): Player {
        val player = assignPlayerToGame(game, session)
        return player
    }

    private fun assignPlayerToGame(game: Game, session: WebSocketSessionWrapper): Player {
        lateinit var player: Player

        if (game.firstPlayer.isConnected().not()) {
            player = game.firstPlayer
            player.session = session
        } else if (game.secondPlayer.isConnected().not()) {
            player = game.secondPlayer
            player.session = session
        } else throw GameLobbyIsFullException()
        return player
    }

    /**
     * Suspends the coroutine until the opponent of the player in the game is connected.
     * If the opponent is already connected, this method returns immediately.
     *
     * @param game The Tic Tac Toe game.
     * @param player The player whose opponent needs to be awaited.
     * @throws TimeoutSecondPlayerException if the second player does not join before the timeout.
     */
    suspend fun awaitOpponent(game: Game, player: Player) {
        if (game.getOpponent(player).isConnected()) return
        if (awaitPlayer(game).not()) {
            throw TimeoutSecondPlayerException()
        }
    }

    /**
     * Suspends the execution of the coroutine until the specified player in the game is connected.
     *
     * @param game The Tic Tac Toe game.
     * @return Returns true if the player is connected within the timeout period, false otherwise.
     */
    private suspend inline fun awaitPlayer(game: Game): Boolean {
        val playerToAwait = if (game.firstPlayer.isConnected()) game.secondPlayer else game.firstPlayer
        val timeout = System.currentTimeMillis() + AWAIT_PLAYER_TIMEOUT_MS
        while (playerToAwait.isConnected().not()) {
            delay(100)
            if (System.currentTimeMillis() > timeout) {
                GameStorage.removeGame(game)
                return false
            }
        }
        return true
    }


    private fun randomGameId(): String {
        return (1..4)
            .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
            .joinToString("")
    }

}

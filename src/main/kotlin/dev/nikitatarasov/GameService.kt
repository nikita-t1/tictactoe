package dev.nikitatarasov

import dev.nikitatarasov.exceptions.GameLobbyIsFullException
import dev.nikitatarasov.exceptions.TimeoutSecondPlayerException
import dev.nikitatarasov.model.Game
import dev.nikitatarasov.model.Player
import dev.nikitatarasov.model.PlayerSymbol
import dev.nikitatarasov.util.GameBoardUtils.checkWinner
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay

object GameService {

    const val AWAIT_PLAYER_TIMEOUT_MS = 1000 * 60 * 5 // 5 min

    fun findGame(gameCode: String): Game? {
        return GameStorage.findGame(gameCode)
    }

    fun generateGameCode(): String {
        var gameCode: String
        do {
            gameCode = Game.randomGameId()
        } while (findGame(gameCode) != null)
        return gameCode
    }

    fun findOrCreateGame(gameCode: String): Game {
        return GameStorage.findOrCreateGame(gameCode)
    }

    fun hasGameWinner(game: Game): Player? {
        val symbol: PlayerSymbol = checkWinner(game.gameBoard) ?: return null
        return when (symbol) {
            game.firstPlayer.symbol -> game.firstPlayer
            game.secondPlayer.symbol -> game.secondPlayer
            else -> null
        }
    }

    suspend fun connectPlayerToGame(game: Game, session: DefaultWebSocketServerSession): Player {
        val player = assignPlayerToGame(game, session)
        return player
    }

    private fun assignPlayerToGame(game: Game, session: DefaultWebSocketServerSession): Player {
        lateinit var player: Player

        // Assign player to game
        if (game.firstPlayer.isConnected().not()) {
            player = game.firstPlayer
            player.session = session
        } else if (game.secondPlayer.isConnected().not()) {
            player = game.secondPlayer
            player.session = session
        } else throw GameLobbyIsFullException()
        return player
    }

    suspend fun awaitOpponent(game: Game, player: Player) {
        if (game.getOpponent(player).isConnected()) return
        if (awaitPlayer(game).not()) {
            throw TimeoutSecondPlayerException()
        }
    }

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
}

package dev.nikitatarasov.model

import dev.nikitatarasov.util.GameBoardUtils
import io.github.oshai.KotlinLogging
import kotlinx.serialization.Serializable
import kotlin.math.log

val logger = KotlinLogging.logger {}

@Serializable
data class WebSocketResponse(
    val gameCode: String = "",
    val statusCode: StatusCode,
    var playerNumber : Int = -1,
    val gameBoard: List<Int> = emptyList(),
    val hasGameEnded: Boolean = false,
    val awaitMoveFromPlayer: Int = -1,
    val bothPlayersConnected: Boolean = false,
    val rematchRequestedByPlayer: Int = -1,
) {
    companion object {
        fun buildResponse(game: Game, player: Player, statusCode: StatusCode): WebSocketResponse {
            val response = WebSocketResponse(
                gameCode = game.id,
                statusCode = statusCode,
                playerNumber = player.symbol.fieldToInt(),
                gameBoard = game.gameBoard.toFlatList(),
                hasGameEnded = GameBoardUtils.checkGameOver(game.gameBoard),
                awaitMoveFromPlayer = game.awaitMoveByPlayer.symbol.fieldToInt(),
                bothPlayersConnected = game.bothPlayersConnected(),
                rematchRequestedByPlayer = game.rematchRequested?.symbol?.fieldToInt() ?: 0
            )
            logger.info { "Built response: $response" }
            return response
        }

        fun buildResponses(game: Game, statusCode: StatusCode): Pair<WebSocketResponse, WebSocketResponse> {
            val playerResponse = WebSocketResponse(
                gameCode = game.id,
                statusCode = statusCode,
                playerNumber = game.firstPlayer.symbol.fieldToInt(),
                gameBoard = game.gameBoard.toFlatList(),
                hasGameEnded = GameBoardUtils.checkGameOver(game.gameBoard),
                awaitMoveFromPlayer = game.awaitMoveByPlayer.symbol.fieldToInt(),
                bothPlayersConnected = game.bothPlayersConnected(),
                rematchRequestedByPlayer = game.rematchRequested?.symbol?.fieldToInt() ?: 0
            )
            val opponentResponse = playerResponse.copy(playerNumber = game.secondPlayer.symbol.fieldToInt())
            logger.info { "Built responses: $playerResponse to $opponentResponse" }
            return playerResponse to opponentResponse
        }
    }

    fun WebSocketResponse.withPlayerNumber(playerNumber: Int): WebSocketResponse {
        return this.copy(playerNumber = playerNumber)
    }
}

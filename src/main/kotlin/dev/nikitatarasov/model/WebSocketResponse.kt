package dev.nikitatarasov.model

import dev.nikitatarasov.util.GameBoardUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable

/**
 * Represents a response for the WebSocket communication.
 *
 * @property gameCode The code of the game.
 * @property statusCode The status code of the response.
 * @property playerNumber The number assigned to the player.
 * @property gameBoard The game board of the game.
 * @property hasGameEnded Indicates whether the game has ended.
 * @property awaitMoveFromPlayer The number representing the player whose move is awaited.
 * @property bothPlayersConnected Indicates whether both players are connected.
 * @property rematchRequestedByPlayer The set of players who requested/accepted a rematch.
 */
@Serializable
data class WebSocketResponse(
    val gameCode: String,
    val statusCode: StatusCode,
    var playerNumber : Int,
    val gameBoard: List<Int>,
    val hasGameEnded: Boolean,
    val awaitMoveFromPlayer: Int,
    val bothPlayersConnected: Boolean,
    val rematchRequestedByPlayer: MutableSet<Int> = mutableSetOf(),
) {


    companion object {

        private val logger = KotlinLogging.logger {}

        /**
         * Builds a WebSocket response for one player based on the game and status code.
         *
         * @param game The Tic Tac Toe game.
         * @param player The player associated with the response.
         * @param statusCode The status code for the response.
         * @return The built WebSocket response.
         */
        fun buildResponse(game: Game, player: Player, statusCode: StatusCode): WebSocketResponse {
            val response = WebSocketResponse(
                gameCode = game.id,
                statusCode = statusCode,
                playerNumber = player.symbol.fieldToInt(),
                gameBoard = game.gameBoard.toFlatList(),
                hasGameEnded = GameBoardUtils.checkGameOver(game.gameBoard),
                awaitMoveFromPlayer = game.awaitMoveByPlayer.symbol.fieldToInt(),
                bothPlayersConnected = game.bothPlayersConnected(),
                rematchRequestedByPlayer = game.rematchRequested.map { it.symbol.fieldToInt() }.toMutableSet()
            )
            logger.debug { "Built response: $response" }
            return response
        }

        /**
         * Builds player and opponent websocket responses based on the game and status code.
         *
         * @param game The game object
         * @param statusCode The status code for the responses
         * @return A Pair of WebSocketResponse objects containing the player and opponent responses
         */
        fun buildResponses(game: Game, statusCode: StatusCode): Pair<WebSocketResponse, WebSocketResponse> {
            val playerResponse = WebSocketResponse(
                gameCode = game.id,
                statusCode = statusCode,
                playerNumber = game.firstPlayer.symbol.fieldToInt(),
                gameBoard = game.gameBoard.toFlatList(),
                hasGameEnded = GameBoardUtils.checkGameOver(game.gameBoard),
                awaitMoveFromPlayer = game.awaitMoveByPlayer.symbol.fieldToInt(),
                bothPlayersConnected = game.bothPlayersConnected(),
                rematchRequestedByPlayer = game.rematchRequested.map { it.symbol.fieldToInt() }.toMutableSet()
            )
            val opponentResponse = playerResponse.copy(playerNumber = game.secondPlayer.symbol.fieldToInt())
            logger.debug { "Built responses: $playerResponse to $opponentResponse" }
            return playerResponse to opponentResponse
        }
    }

}

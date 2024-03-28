package dev.nikitatarasov

import dev.nikitatarasov.exceptions.NoGameCodeException
import dev.nikitatarasov.exceptions.WebSocketException
import dev.nikitatarasov.model.*
import dev.nikitatarasov.model.WebSocketResponse.Companion.buildResponse
import dev.nikitatarasov.model.WebSocketResponse.Companion.buildResponses
import dev.nikitatarasov.util.GameBoardUtils.checkGameOver
import dev.nikitatarasov.wrapper.WebSocketSessionWrapper
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

private val logger = KotlinLogging.logger {}

suspend fun startSession(session: WebSocketSessionWrapper) {
    var game: Game? = null // Game is null until it's created or found
    var response: WebSocketResponse
    var responses: Pair<WebSocketResponse, WebSocketResponse>
    try {
        val gameCode = session.call.request.queryParameters["gameCode"]?.uppercase()
        if (gameCode.isNullOrBlank()) throw NoGameCodeException()
        game = GameStorage.findOrCreateGame(gameCode)

        // Assign player to game
        val player = GameService.connectPlayerToGame(game, session)
        if (game.bothPlayersConnected().not()) {
            response = buildResponse(game, player, StatusCode.NO_OPPONENT_YET)
            sendToPlayer(session, response)

            // Await opponent
            GameService.awaitOpponent(game, player) // blocks until second player joins
        }

        // Both players connected
        responses = buildResponses(game, StatusCode.BOTH_PLAYERS_CONNECTED)
        sendToBothPlayers(game, responses.first, responses.second)

        // Game can Start...
        responses = buildResponses(game, StatusCode.GAME_STARTED)
        sendToBothPlayers(game, responses.first, responses.second)

        handleIncomingFrames(game, session)

    } catch (e: WebSocketException) {
        session.sendSerialized(e.webSocketDataCode)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {

        val disconnectedPlayer = game?.getPlayerBySession(session)
        val stillConnectedPlayer = game?.getOpponent(disconnectedPlayer!!)
        game?.removePlayer(session)
        // check if the other player is still connected
        if (stillConnectedPlayer?.isConnected() == true) {
            response = buildResponse(game!!, stillConnectedPlayer, StatusCode.OPPONENT_DISCONNECTED)
            sendToPlayer(stillConnectedPlayer.session!!, response)
        }

        session.close()
    }
}

/**
 * Handles incoming frames from the client
 *
 * This function is the main entry point for handling incoming frames from the client.
 *
 * @param game The game object
 */
private suspend fun handleIncomingFrames(game: Game, session: WebSocketSessionWrapper) {
    var counter = 0

    for (frame in session.incoming) {
        if (frame is Frame.Text) {
            logger.trace { "Frame #${++counter}" }
            val request = Json.decodeFromString<WebSocketRequest>(frame.readText())
            logger.debug { "Received: $request" }
            val player = game.getPlayerBySession(session)!!

            if (checkGameOver(game.gameBoard)){
                // if we get a message after the game is over, it MUST be a rematch request
                handleRematchRequest(game, session, request)
                continue // skip the rest of this frame
            }

            /**
             * every frame from here on is a move request
             */

            // if the game is not over, and the board is not full, and it's the player's turn
            if (game.awaitMoveByPlayer == player){
                handlePlayerMove(game, player, request.playerMoveAtIndex!!)
            } // else, it's not the player's turn, so ignore the move request

            // check if the game is over after the move
            if (checkGameOver(game.gameBoard)){
                // the exact end condition will be evaluated client-side
                val responses = buildResponses(game, StatusCode.GAME_ENDED)
                sendToBothPlayers(game, responses.first, responses.second)
                continue // skip the rest of this frame
            }
        }
    }
}

suspend fun handleRematchRequest(game: Game, session: WebSocketSessionWrapper, request: WebSocketRequest) {
    if (request.rematchRequestedByPlayer) {
        val player = game.getPlayerBySession(session)
        val opponent = game.getOpponent(player!!)

        if (game.rematchRequested.contains(opponent)){
            // if the opponent has already requested a rematch
            game.rematchRequested.add(player)
            val gameCode = GameService.generateGameCode()
            val nextGame = GameStorage.findOrCreateGame(gameCode)
            val responses = buildResponses(nextGame, StatusCode.REMATCH_ACCEPTED)
            sendToBothPlayers(game, responses.first, responses.second)
        } else {
            // if the opponent has not requested a rematch
            game.rematchRequested.add(player)
            val responses = buildResponses(game, StatusCode.REMATCH_REQUESTED)
            sendToBothPlayers(game, responses.first, responses.second)
        }
    }

}

private suspend fun handlePlayerMove(game: Game, player: Player, move: Int) {
    val isValidMove = game.gameBoard.setSymbol(player, move)
    if (isValidMove) {
        val playerResponse = buildResponse(game, player, StatusCode.MOVE_ACCEPTED)
        sendToPlayer(game.awaitMoveByPlayer.session!!, playerResponse)

        game.awaitMoveByPlayer = game.getOpponent(player)
        val opponentResponse = buildResponse(game, game.awaitMoveByPlayer, StatusCode.YOUR_MOVE)
        sendToPlayer(game.awaitMoveByPlayer.session!!, opponentResponse)
    } else {
        val response = buildResponse(game, player, StatusCode.INVALID_MOVE)
        sendToPlayer(game.awaitMoveByPlayer.session!!, response) // send only to the player who made the invalid move
    }
}

private suspend fun sendToBothPlayers(game: Game, toFirst: WebSocketResponse, toSecond: WebSocketResponse) {
    game.firstPlayer.session?.sendSerialized(toFirst)
    game.secondPlayer.session?.sendSerialized(toSecond)
}

private suspend fun sendToPlayer(session: WebSocketSessionWrapper, response: WebSocketResponse) {
    session.sendSerialized(response)
}



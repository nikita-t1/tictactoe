package dev.nikitatarasov

import dev.nikitatarasov.exceptions.GameLobbyIsFullException
import dev.nikitatarasov.exceptions.NoGameCodeException
import dev.nikitatarasov.exceptions.TimeoutSecondPlayerException
import dev.nikitatarasov.exceptions.WebSocketException
import dev.nikitatarasov.model.Game
import dev.nikitatarasov.model.WebSocketDataCode
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText

@Suppress("NestedBlockDepth")
suspend fun DefaultWebSocketServerSession.startSession(){
    var game: Game? = null
    try {
        println("connect")
        val gameCode = call.request.queryParameters["gameCode"]?.uppercase()
        if (gameCode.isNullOrBlank()) throw NoGameCodeException()

        game = Controller.findGame(gameCode) ?: Controller.createGame(gameCode)

        assignPlayersToGame(game, this)

        for (frame in incoming) {
            if (frame is Frame.Text) {
                val text = frame.readText()

                // If Move comes from expected Player
                if (this == game.awaitMoveByPlayer.session) {
                    handleIncomingFrame(game, text)

                } else this.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.NOT_YOUR_TURN))

            }
        }
    } catch (e: WebSocketException) {
        sendSerialized(e.webSocketDataCode)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        removePlayerFromGame(game, this)
        this.close()
    }
}


private suspend fun removePlayerFromGame(game: Game?, session: DefaultWebSocketServerSession) {
    game?.let {
        val player = game.getPlayerBySession(session)
        player?.let {
            game.getOpponent(player).session?.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_DISCONNECTED))
        }
        game.removePlayer(session)
        if (game.firstPlayer.session == null && game.secondPlayer.session == null){
            Controller.removeGame(game)
        }
    }
}


private suspend fun handleIncomingFrame(
    game: Game,
    text: String
) {
    val isValidMove = game.gameBoard.setSymbol(game.awaitMoveByPlayer, text.toInt())
    if (isValidMove) {
        game.awaitMoveByPlayer.session?.let {
            it.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.MOVE_ACCEPTED))
            it.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_MOVE))
        }
        println(game.awaitMoveByPlayer.session)
        game.awaitMoveByPlayer = game.getOpponent(game.awaitMoveByPlayer)
        println(game.awaitMoveByPlayer.session)
        game.awaitMoveByPlayer.session?.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOUR_MOVE))

        sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_BOARD, game.gameBoard.toJSONList()))

    } else {
        game.awaitMoveByPlayer.session?.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.MOVE_INVALID))
    }

    val winner = game.hasGameWinner()
    when (winner) {
        game.firstPlayer -> {
            sendToPlayers(
                game,
                toFirst = WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOU_WON),
                toSecond = WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_WON)
            )
        }

        game.secondPlayer -> {
            sendToPlayers(
                game,
                toFirst = WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_WON),
                toSecond = WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOU_WON)
            )
        }
    }
    if (winner != null) {
        // game close
        // ask the players if they want another game... Same socket or new one?
        game.firstPlayer.session!!.close()
        game.secondPlayer.session!!.close()
    }
}

private suspend fun sendToPlayers(game: Game, toFirst: WebSocketDataCode, toSecond: WebSocketDataCode? = null) {
    game.firstPlayer.session?.sendSerialized(toFirst)
    when (toSecond){
        null -> game.secondPlayer.session?.sendSerialized(toFirst)
        else -> game.secondPlayer.session?.sendSerialized(toSecond)
    }
}


@Suppress("ThrowsCount")
private suspend fun assignPlayersToGame(game: Game, session: DefaultWebSocketServerSession) {
    if (game.firstPlayer.isReady().not()) {
        game.firstPlayer.session = session

        when (game.secondPlayer.isReady()) {
            true -> sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.SECOND_PLAYER_CONNECTED))
            false -> {
                sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.NO_SECOND_PLAYER_YET))
                if (Controller.awaitPlayer(game).not()) {
                    throw TimeoutSecondPlayerException()
                }
                sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.SECOND_PLAYER_CONNECTED))
            }
        }

    } else if (game.secondPlayer.isReady().not()) {
        game.secondPlayer.session = session

        when (game.firstPlayer.isReady()) {
            true -> sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.FIRST_PLAYER_CONNECTED))
            false -> {
                sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.NO_FIRST_PLAYER_YET))
                if (Controller.awaitPlayer(game).not()) {
                    throw TimeoutSecondPlayerException()
                }
                sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.FIRST_PLAYER_CONNECTED))
            }
        }

    } else throw GameLobbyIsFullException()

    // Game can Start...
    game.awaitMoveByPlayer.session?.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOUR_MOVE))
    game.getOpponent(game.awaitMoveByPlayer).session?.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_MOVE))
    sendToPlayers(game, WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_BOARD, game.gameBoard.toJSONList()))
}

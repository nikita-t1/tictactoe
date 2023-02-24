package dev.nikitatarasov

import dev.nikitatarasov.exceptions.GameLobbyIsFullException
import dev.nikitatarasov.exceptions.NoGameCodeException
import dev.nikitatarasov.exceptions.TimeoutSecondPlayerException
import dev.nikitatarasov.exceptions.WebSocketException
import dev.nikitatarasov.model.Game
import dev.nikitatarasov.model.Player
import dev.nikitatarasov.model.WebSocketDataCode as WSDataCode
import dev.nikitatarasov.model.WebSocketDataCode.Companion.StatusCode as StatusCode
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

                if (game.hasGameWinner() == null) { // Game is still going
                    // If Move comes from expected Player
                    if (this == game.awaitMoveByPlayer.session) {
                        handleIncomingFrame(game, text)

                    } else this.sendSerialized(WSDataCode(StatusCode.NOT_YOUR_TURN))
                    handleWinnerCheck(game)
                } else { // Game is over
                    handleRematchRequest(game, text, this)
                }

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

private suspend fun handleRematchRequest(game: Game, text: String, session: DefaultWebSocketServerSession){
    println(text)
    when (text) {
        StatusCode.REQUEST_REMATCH.code.toString() -> {
            game.rematchRequested = true
            sendToOpponent(game, session,  WSDataCode(StatusCode.REMATCH_REQUESTED))
        }
        StatusCode.ACCEPT_REMATCH.code.toString() -> {
            if (game.rematchRequested) {
                val newGameCode = Game.randomGameId()
                sendToPlayers(game, WSDataCode(StatusCode.REMATCH_ACCEPTED, newGameCode))
            }
        }
    }
}

private suspend fun removePlayerFromGame(game: Game?, session: DefaultWebSocketServerSession) {
    game?.let {
        val player = game.getPlayerBySession(session)
        player?.let {
            game.getOpponent(player).session?.sendSerialized(WSDataCode(StatusCode.OPPONENT_DISCONNECTED))
        }
        game.removePlayer(session)
        if (game.firstPlayer.session == null && game.secondPlayer.session == null){
            Controller.removeGame(game)
        }
    }
}

private suspend fun handleIncomingFrame(game: Game, text: String) {
    val isValidMove = game.gameBoard.setSymbol(game.awaitMoveByPlayer, text.toInt())
    if (isValidMove) {
        game.awaitMoveByPlayer.session?.let {
            it.sendSerialized(WSDataCode(StatusCode.MOVE_ACCEPTED))
            it.sendSerialized(WSDataCode(StatusCode.OPPONENT_MOVE))
        }
        println(game.awaitMoveByPlayer.session)
        game.awaitMoveByPlayer = game.getOpponent(game.awaitMoveByPlayer)
        println(game.awaitMoveByPlayer.session)
        game.awaitMoveByPlayer.session?.sendSerialized(WSDataCode(StatusCode.YOUR_MOVE))

        sendToPlayers(game, WSDataCode(StatusCode.GAME_BOARD, game.gameBoard.toJSONList()))

    } else {
        game.awaitMoveByPlayer.session?.sendSerialized(WSDataCode(StatusCode.MOVE_INVALID))
    }
}

private suspend fun handleWinnerCheck(game: Game): Player? {
    val winner: Player? = game.hasGameWinner()
    when (winner) {
        game.firstPlayer -> {
            sendToPlayers(
                game,
                toFirst = WSDataCode(StatusCode.YOU_WON),
                toSecond = WSDataCode(StatusCode.OPPONENT_WON)
            )
        }

        game.secondPlayer -> {
            sendToPlayers(
                game,
                toFirst = WSDataCode(StatusCode.OPPONENT_WON),
                toSecond = WSDataCode(StatusCode.YOU_WON)
            )
        }
    }

    return winner
}

private suspend fun sendToPlayers(game: Game, toFirst: WSDataCode, toSecond: WSDataCode? = null) {
    game.firstPlayer.session?.sendSerialized(toFirst)
    when (toSecond){
        null -> game.secondPlayer.session?.sendSerialized(toFirst)
        else -> game.secondPlayer.session?.sendSerialized(toSecond)
    }
}

private suspend fun sendToOpponent(game: Game, session: DefaultWebSocketServerSession, data: WSDataCode){
    val player = game.getPlayerBySession(session) ?: return
    val opponent = game.getOpponent(player)
    opponent.session?.sendSerialized(data)
}

@Suppress("ThrowsCount")
private suspend fun assignPlayersToGame(game: Game, session: DefaultWebSocketServerSession) {
    if (game.firstPlayer.isReady().not()) {
        game.firstPlayer.session = session

        when (game.secondPlayer.isReady()) {
            true -> sendToPlayers(game, WSDataCode(StatusCode.SECOND_PLAYER_CONNECTED))
            false -> {
                sendToPlayers(game, WSDataCode(StatusCode.NO_SECOND_PLAYER_YET))
                if (Controller.awaitPlayer(game).not()) {
                    throw TimeoutSecondPlayerException()
                }
                sendToPlayers(game, WSDataCode(StatusCode.SECOND_PLAYER_CONNECTED))
            }
        }

    } else if (game.secondPlayer.isReady().not()) {
        game.secondPlayer.session = session

        when (game.firstPlayer.isReady()) {
            true -> sendToPlayers(game, WSDataCode(StatusCode.FIRST_PLAYER_CONNECTED))
            false -> {
                sendToPlayers(game, WSDataCode(StatusCode.NO_FIRST_PLAYER_YET))
                if (Controller.awaitPlayer(game).not()) {
                    throw TimeoutSecondPlayerException()
                }
                sendToPlayers(game, WSDataCode(StatusCode.FIRST_PLAYER_CONNECTED))
            }
        }

    } else throw GameLobbyIsFullException()

    // Game can Start...
    game.awaitMoveByPlayer.session?.sendSerialized(WSDataCode(StatusCode.YOUR_MOVE))
    game.getOpponent(game.awaitMoveByPlayer).session?.sendSerialized(WSDataCode(StatusCode.OPPONENT_MOVE))
    sendToPlayers(game, WSDataCode(StatusCode.GAME_BOARD, game.gameBoard.toJSONList()))
}

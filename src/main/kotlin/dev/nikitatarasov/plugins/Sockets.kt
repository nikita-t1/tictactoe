package dev.nikitatarasov.plugins

import dev.nikitatarasov.Controller
import dev.nikitatarasov.exceptions.GameLobbyIsFullException
import dev.nikitatarasov.exceptions.NoGameCodeException
import dev.nikitatarasov.exceptions.TimeoutSecondPlayerException
import dev.nikitatarasov.exceptions.WebSocketException
import dev.nikitatarasov.model.Game
import dev.nikitatarasov.model.WebSocketDataCode
import io.ktor.serialization.kotlinx.*
import dev.nikitatarasov.model.WebSocketDataCode as WSDataCode
import dev.nikitatarasov.model.WebSocketDataCode.Companion.StatusCode as StatusCode
import io.ktor.server.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }

    routing {
        webSocket("/ws") { // websocketSession
            var game: Game? = null
            try {
                println("connect")
                val gameCode = call.request.queryParameters["gameCode"]?.uppercase()
                if (gameCode.isNullOrBlank()) {
                    throw NoGameCodeException()
                }

                game = Controller.findGame(gameCode) ?: Controller.createGame(gameCode)

                assignPlayersToGame(game)

                game.awaitMoveByPlayer.session?.sendSerialized(WSDataCode(StatusCode.YOUR_MOVE))
                game.getOpponent(game.awaitMoveByPlayer).session?.sendSerialized(WSDataCode(StatusCode.OPPONENT_MOVE))

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()

                        // If Move comes from expected Player
                        if (this == game.awaitMoveByPlayer.session) {
                            val isValidMove = game.gameBoard.setSymbol(game.awaitMoveByPlayer, text.toInt())
                            if (isValidMove) {
                                game.awaitMoveByPlayer.session?.let {
                                    sendSerialized(WSDataCode(StatusCode.MOVE_ACCEPTED))
                                    sendSerialized(WSDataCode(StatusCode.OPPONENT_MOVE))
                                }
                                println(game.awaitMoveByPlayer.session)
                                game.awaitMoveByPlayer = game.getOpponent(game.awaitMoveByPlayer)
                                println(game.awaitMoveByPlayer.session)
                                game.awaitMoveByPlayer.session?.sendSerialized(WSDataCode(StatusCode.YOUR_MOVE))

                                sendToPlayers(game, WSDataCode(StatusCode.GAME_BOARD, game.gameBoard.toJSONList()))

                            } else {
                                game.awaitMoveByPlayer.session?.sendSerialized(WSDataCode(StatusCode.MOVE_INVALID))
                            }

                            val winner = game.hasGameWinner()
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
                            if (winner != null) {
                                // game close
                                // ask the players if they want another game... Same socket or new one?
                                game.firstPlayer.session!!.close()
                                game.secondPlayer.session!!.close()
                            }

                        } else {
                            game.getOpponent(game.awaitMoveByPlayer).session!!.sendSerialized(WSDataCode(StatusCode.NOT_YOUR_TURN))
                            println("ignore")
                            // Ignore
                        }
                    }
                }
            } catch (e: WebSocketException) {
                sendSerialized(e.webSocketDataCode)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                game?.let {
                    val player = game.getPlayerBySession(this)
                    player?.let {
                        game.getOpponent(player).session?.sendSerialized(WSDataCode(StatusCode.OPPONENT_DISCONNECTED))
                    }
                    game.removePlayer(this)
                }
                if (game != null && game.firstPlayer.session == null && game.secondPlayer.session == null){
                    Controller.removeGame(game)
                }
                close()
                println("finally")
            }

        }
    }
}

private suspend fun sendToPlayers(game: Game, toFirst: WebSocketDataCode, toSecond: WebSocketDataCode? = null) {
    game.firstPlayer.session?.sendSerialized(toFirst)
    if (toSecond == null) {
        game.secondPlayer.session?.sendSerialized(toFirst)
    } else {
        game.secondPlayer.session?.sendSerialized(toSecond)
    }
}


private suspend fun DefaultWebSocketServerSession.assignPlayersToGame(game: Game) {
    if (game.firstPlayer.isReady().not()) {
        game.firstPlayer.session = this

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
        game.secondPlayer.session = this

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
}


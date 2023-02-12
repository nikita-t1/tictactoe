package dev.nikitatarasov.plugins

import dev.nikitatarasov.Controller
import dev.nikitatarasov.exceptions.GameLobbyIsFullException
import dev.nikitatarasov.exceptions.NoGameCodeException
import dev.nikitatarasov.exceptions.TimeoutSecondPlayerException
import dev.nikitatarasov.exceptions.WebSocketException
import dev.nikitatarasov.model.WebSocketDataCode
import dev.nikitatarasov.model.WebSocketDataCode.Companion.StatusCode as StatusCode
import io.ktor.serialization.kotlinx.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.util.*

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
            try {
                println("connect")
                val gameCode = call.request.queryParameters["gameCode"]?.uppercase()
                if (gameCode.isNullOrBlank()) {
                    throw NoGameCodeException()
                }

                val game = Controller.findGame(gameCode) ?: Controller.createGame(gameCode)

                if (game.firstPlayer.isReady().not()) {
                    game.firstPlayer.session = this
                    sendSerialized(WebSocketDataCode(StatusCode.NO_SECOND_PLAYER_YET))
                    if (Controller.awaitSecondPlayer(game).not()) {
                        throw TimeoutSecondPlayerException()
                    }
                    sendSerialized(WebSocketDataCode(StatusCode.SECOND_PLAYER_CONNECTED))

                } else {
                    println("here")
                    if (game.secondPlayer.isReady()) {
                        throw GameLobbyIsFullException()
                    }

                    game.secondPlayer.session = this
                    sendSerialized(WebSocketDataCode(StatusCode.STATUS_OK))
                }

                game.firstPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.YOUR_MOVE))
                game.secondPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.OPPONENT_MOVE))

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()

                        // If Move comes from expected Player
                        if (this == game.awaitMoveByPlayer.session) {
                            val isValidMove = game.gameBoard.setSymbol(game.awaitMoveByPlayer, text.toInt())
                            if (isValidMove) {
                                game.awaitMoveByPlayer.session!!.sendSerialized(
                                    WebSocketDataCode(
                                        StatusCode.MOVE_ACCEPTED
                                    )
                                )
                                game.awaitMoveByPlayer.session!!.sendSerialized(
                                    WebSocketDataCode(
                                        StatusCode.GAME_BOARD,
                                        game.gameBoard.toJSON()
                                    )
                                )
                                game.awaitMoveByPlayer.session!!.sendSerialized(
                                    WebSocketDataCode(
                                        StatusCode.OPPONENT_MOVE
                                    )
                                )
                                game.awaitMoveByPlayer = game.getOpponent(game.awaitMoveByPlayer)
                                game.awaitMoveByPlayer.session!!.sendSerialized(
                                    WebSocketDataCode(
                                        StatusCode.GAME_BOARD,
                                        game.gameBoard.toJSON()
                                    )
                                )
                                game.awaitMoveByPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.YOUR_MOVE))

                            } else {
                                game.awaitMoveByPlayer.session!!.sendSerialized(
                                    WebSocketDataCode(
                                        StatusCode.MOVE_INVALID
                                    )
                                )
                            }

                            val winner = game.hasGameWinner()
                            println("winner $winner")
                            if (winner != null) {
                                if (winner == game.firstPlayer) {
                                    println("first player won")
                                    game.firstPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.YOU_WON))
                                    game.secondPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.OPPONENT_WON))
                                } else if (winner == game.secondPlayer) {
                                    println("second player won")
                                    game.secondPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.YOU_WON))
                                    game.firstPlayer.session!!.sendSerialized(WebSocketDataCode(StatusCode.OPPONENT_WON))
                                }

                                // game close
                                // ask the players if they want another game... Same socket or new one?
                                game.firstPlayer.session!!.close()
                                game.secondPlayer.session!!.close()
                            }
                        } else {
                            game.getOpponent(game.awaitMoveByPlayer).session!!.sendSerialized(
                                WebSocketDataCode(
                                    StatusCode.NOT_YOUR_TURN
                                )
                            )
                            println("ignore")
                            // Ignore
                        }
                    }
                }
                println("WebSocket closed")
            } catch (e: WebSocketException) {
                sendSerialized(e.webSocketDataCode)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                close()
                println("finally")
            }

        }
    }
}




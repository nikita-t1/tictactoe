package dev.nikitatarasov.plugins

import dev.nikitatarasov.Controller
import dev.nikitatarasov.model.WebSocketDataCode
import io.ktor.serialization.kotlinx.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
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
            try {
                println("connect")
                val gameCode = call.request.queryParameters["gameCode"]
                if (gameCode.isNullOrBlank()) {
                    sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.NO_GAMECODE))
                    close()
                    return@webSocket
                }

                val game = Controller.findGame(gameCode) ?: Controller.createGame(gameCode)

                if (game.firstPlayer.isReady().not()) {
                    game.firstPlayer.session = this
                    sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.NO_SECOND_PLAYER_YET))
                    if (Controller.awaitSecondPlayer(game).not()) {
                        sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_CODE_TIMEOUT_REACHED))
                        close()
                        return@webSocket
                    }
                    sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.SECOND_PLAYER_CONNECTED))

                } else {
                    println("here")
                    if (game.secondPlayer.isReady()){
                        sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_ALREADY_HAS_TWO_PLAYERS))
                        close()
                        return@webSocket
                    }

                    game.secondPlayer.session = this
                    sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.STATUS_OK))
                }

                game.firstPlayer.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOUR_MOVE))
                game.secondPlayer.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_MOVE))

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()

                        // If Move comes from expected Player
                        if (this == game.awaitMoveByPlayer.session){
                            val isValidMove = game.gameBoard.setSymbol(game.awaitMoveByPlayer, text.toInt())
                            if (isValidMove){
                                game.awaitMoveByPlayer!!.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.MOVE_ACCEPTED))
                                game.awaitMoveByPlayer!!.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_BOARD, game.gameBoard.toJSON()))
                                game.awaitMoveByPlayer.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_MOVE))
                                game.awaitMoveByPlayer = game.getOpponent(game.awaitMoveByPlayer!!)
                                game.awaitMoveByPlayer!!.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_BOARD, game.gameBoard.toJSON()))
                                game.awaitMoveByPlayer.session!!.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOUR_MOVE))

                        game.awaitMoveByPlayer!!.session.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_BOARD, game.gameBoard.toJSON()))
                    } else {
                        println("ignore")
                        // Ignore
                    }
//                    outgoing.send(Frame.Text("YOU SAID: $text"))
//                    if (text.equals("bye", ignoreCase = true)) {
//                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
//                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                println("finally")
            }

        }
    }
}




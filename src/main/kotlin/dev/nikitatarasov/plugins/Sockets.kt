package dev.nikitatarasov.plugins

import dev.nikitatarasov.model.Game
import dev.nikitatarasov.model.Player
import dev.nikitatarasov.model.PlayerSymbol
import dev.nikitatarasov.model.WebSocketDataCode
import io.ktor.serialization.kotlinx.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.collections.LinkedHashSet

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
            val gameCode = call.request.queryParameters["gameCode"]
            if (gameCode.isNullOrBlank()) {
                close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                return@webSocket
            }
            val game: Game
            val player: Player

            val foundGame = Controller.games.find { it.id == gameCode }
            if (foundGame == null) {
                player = Player(symbol = PlayerSymbol.CROSS, session = this)
                game = Game(id = gameCode, firstPlayer = player)
                Controller.games.add(game)
                val timeout = System.currentTimeMillis() + (1000 * 60 * 5) // 5 min
                delay(5000)
                while (game.secondPlayer == null) {
                    delay(100)
                    if (System.currentTimeMillis() > timeout){
                        Controller.games.remove(game)
                        sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_CODE_TIMEOUT_REACHED))
                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                        return@webSocket
                    }
                }
                sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.SECOND_PLAYER_CONNECTED))
            } else {
                if (foundGame.secondPlayer != null){
                    sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_ALREADY_HAS_TWO_PLAYERS))
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                    return@webSocket
                }
                player = Player(symbol = PlayerSymbol.NOUGHT, session = this)
                foundGame.secondPlayer = player
                game = foundGame
                sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.STATUS_OK))
            }

            val opponent: Player? = game.getOpponent(player)
            if (opponent == null){
                sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.NO_SECOND_PLAYER_YET))
                close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                return@webSocket
            }

            game.firstPlayer!!.session.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.YOUR_MOVE))
            game.firstPlayer.session.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.OPPONENT_MOVE))

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    if (this == game.awaitMoveByPlayer!!.session){
                        game.gameBoard.setSymbol(game.awaitMoveByPlayer!!, text.toInt())
                        game.awaitMoveByPlayer!!.session.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.MOVE_ACCEPTED))

                        game.awaitMoveByPlayer!!.session.sendSerialized(WebSocketDataCode(WebSocketDataCode.Companion.StatusCode.GAME_BOARD, game.gameBoard.toJSON()))
                        game.awaitMoveByPlayer = game.getOpponent(game.awaitMoveByPlayer!!)

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
            }
        }
    }
}

object Controller {

    val games: MutableSet<Game> = Collections.synchronizedSet<Game>(LinkedHashSet())

}

package dev.nikitatarasov.plugins

import dev.nikitatarasov.model.Game
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/create-game-code") {
            var gameCode: String
            do {
                gameCode = Game.randomGameId()
            } while (Controller.games.any { it.id == gameCode })
            call.respondText { gameCode }
        }
    }
}

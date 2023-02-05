package dev.nikitatarasov.plugins

import dev.nikitatarasov.Controller
import dev.nikitatarasov.model.Game
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*

fun Application.configureRouting() {
    routing {
        post("/create-game-code") {
            var gameCode: String
            do {
                gameCode = Game.randomGameId()
            } while (Controller.findGame(gameCode) != null)
            call.respondText { gameCode }
        }
        get("/gameCodes") {
            call.respondText { Controller.getAllGames().toString() }
        }
        singlePageApplication {
            useResources = true
            vue("dist")
        }
    }
}

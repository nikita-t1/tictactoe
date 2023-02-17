package dev.nikitatarasov.plugins

import dev.nikitatarasov.Controller
import dev.nikitatarasov.model.Game
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit

fun Application.configureRouting() {
    routing {
        post("/create-game-code") {
            // remove old games
            Controller.removeExpiredGames()

            var gameCode: String
            do {
                gameCode = Game.randomGameId()
            } while (Controller.findGame(gameCode) != null)
            call.respondText { gameCode }
        }
        get("/gameCodes") {
            call.respond(Controller.getAllGames()!!.toList())
        }
        singlePageApplication {
            useResources = true
            vue("dist")
        }
    }
}

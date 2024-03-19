package dev.nikitatarasov.plugins

import dev.nikitatarasov.GameService
import dev.nikitatarasov.GameStorage
import dev.nikitatarasov.model.Game
import io.github.oshai.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val logger = KotlinLogging.logger {}

fun Application.configureRouting() {
    routing {
        post("/create-game-code") {
            // remove old games
            GameStorage.removeExpiredGames()

            var gameCode: String
            do {
                gameCode = Game.randomGameId()
            } while (GameService.findGame(gameCode) != null)
            call.respondText { gameCode }
        }
        get("/gameCodes") {
            call.respond(GameStorage.getAllGames())
        }
        get("/dev") {
            call.respond(call.application.environment.developmentMode)
        }
        singlePageApplication {
            useResources = true
            vue("dist")
        }
    }
}

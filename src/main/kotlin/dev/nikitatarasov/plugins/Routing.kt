package dev.nikitatarasov.plugins

import dev.nikitatarasov.GameService
import dev.nikitatarasov.GameStorage
import io.github.oshai.kotlinlogging.KotlinLogging
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

            val gameCode = GameService.generateGameCode()
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

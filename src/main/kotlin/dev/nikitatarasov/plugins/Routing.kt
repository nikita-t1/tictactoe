package dev.nikitatarasov.plugins

import dev.nikitatarasov.Controller
import dev.nikitatarasov.model.Game
import io.github.oshai.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.http.content.vue
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

private val logger = KotlinLogging.logger {}

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

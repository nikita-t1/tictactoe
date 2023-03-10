package dev.nikitatarasov

import dev.nikitatarasov.model.Game
import io.github.oshai.KotlinLogging
import kotlinx.coroutines.delay
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.LinkedHashSet

object Controller {

    private val logger = KotlinLogging.logger {}
    private val games: MutableSet<Game> = Collections.synchronizedSet(LinkedHashSet())

    fun getAllGames() = games as? Set<Game>

    fun findGame(gameCode: String): Game? {
        synchronized(games){
            return games.find { it.id == gameCode }
        }
    }

    fun removeGame(game: Game){
        synchronized(games) {
            games.remove(game)
        }
    }

    fun createGame(gameCode: String): Game {
        synchronized(games) {
            val game = Game(gameCode)
            games.add(game)
            return game
        }
    }

    fun removeExpiredGames(expirationTime: Int = 60){
        synchronized(games) {
            getAllGames()
                ?.filter { ChronoUnit.MINUTES.between(it.creationTime.toJavaLocalDateTime(), LocalDateTime.now()) >= expirationTime }
                ?.forEach {
                    logger.info{"Remove Game Code: ${it.id}"}
                    removeGame(it)
                }
        }
    }

    suspend inline fun awaitPlayer(game: Game): Boolean {
        val playerToAwait = if (game.firstPlayer.isReady()) game.secondPlayer else game.firstPlayer
        val timeout = System.currentTimeMillis() + (1000 * 60 * 5) // 5 min
        while (playerToAwait.isReady().not()) {
            delay(100)
            if (System.currentTimeMillis() > timeout){
                removeGame(game)
                return false
            }
        }
        return true
    }

}

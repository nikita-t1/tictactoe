package dev.nikitatarasov

import dev.nikitatarasov.model.Game
import kotlinx.coroutines.delay
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.LinkedHashSet

object Controller {

    private val games: MutableSet<Game> = Collections.synchronizedSet(LinkedHashSet())

    fun getAllGames() = games as? Set<Game>

    fun findGame(gameCode: String): Game? {
        return games.find { it.id == gameCode }
    }

    fun removeGame(game: Game){
        games.remove(game)
    }

    fun createGame(gameCode: String): Game {
        val game = Game(gameCode)
        games.add(game)
        return game
    }

    fun removeExpiredGames(expirationTime: Int = 60){
        // Coroutine?
        getAllGames()
            ?.filter { ChronoUnit.MINUTES.between(it.creationTime.toJavaLocalDateTime(), LocalDateTime.now()) >= expirationTime }
            ?.forEach {
                println("Remove Game Code: ${it.id}")
                removeGame(it)
            }
    }

    suspend inline fun awaitSecondPlayer(game: Game): Boolean {
        val timeout = System.currentTimeMillis() + (1000 * 60 * 5) // 5 min
        while (game.secondPlayer.isReady().not()) {
            delay(100)
            if (System.currentTimeMillis() > timeout){
                removeGame(game)
                return false
            }
        }
        return true
    }

}

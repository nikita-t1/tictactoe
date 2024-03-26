package dev.nikitatarasov

import dev.nikitatarasov.model.Game
import io.github.oshai.KotlinLogging
import kotlinx.datetime.toJavaLocalDateTime
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.LinkedHashSet

/**
 * The GameStorage class provides storage and operations for managing Tic Tac Toe games.
 */
object GameStorage {

    private val logger = KotlinLogging.logger {}

    private val games: MutableSet<Game> = Collections.synchronizedSet(LinkedHashSet())

    fun getAllGames() = games.toSet()

    fun findGame(gameCode: String): Game? {
        synchronized(games){
            return games.find { it.id == gameCode }
        }
    }

    fun findOrCreateGame(gameCode: String): Game {
        return findGame(gameCode) ?: createGame(gameCode)
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

    /**
     * Removes expired games from the game storage.
     *
     * This method is executed on every new gameCode creation.
     *
     * @param expirationTime The expiration time in minutes. Default value is 60 minutes.
     */
    fun removeExpiredGames(expirationTime: Int = 60){
        synchronized(games) {
            getAllGames()
                .filter { ChronoUnit.MINUTES.between(it.creationTime.toJavaLocalDateTime(), LocalDateTime.now()) >= expirationTime }
                .forEach {
                    logger.info{"Remove Game Code: ${it.id}"}
                    removeGame(it)
                }
        }
    }}

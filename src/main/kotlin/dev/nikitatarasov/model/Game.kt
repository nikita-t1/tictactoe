package dev.nikitatarasov.model

import java.util.*
import kotlin.collections.LinkedHashSet
import kotlin.random.Random

data class Game(
    val id: String = randomGameId(),
    val firstPlayer: Player? = null,
    var secondPlayer: Player? = null,
) {
    val gameBoard: GameBoard = GameBoard()

    companion object {
        val games = Collections.synchronizedSet<Game>(LinkedHashSet())

        private val charPool: List<Char> = ('A'..'Z') + ('0'..'9')
        fun randomGameId(): String {
            return (1..4)
                .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
                .joinToString("")
        }
    }

}




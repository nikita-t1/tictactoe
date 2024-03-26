package dev.nikitatarasov.model

import io.github.oshai.KotlinLogging
import kotlinx.serialization.Serializable

/**
 * Represents the game board for Tic-Tac-Toe.
 */
@Serializable
class GameBoard {

    private val logger = KotlinLogging.logger {}

    val gameField: List<MutableList<PlayerSymbol>> = List(3) { MutableList(3) { PlayerSymbol.UNSET } }

    /**
     * Sets the symbol for a player on the game board.
     *
     * @param player The player whose symbol needs to be set.
     * @param index The index at which the symbol needs to be set on the game board.
     * @return Returns true if the symbol was set successfully, false otherwise.
     */
    fun setSymbol(player: Player, index: Int): Boolean {
        val row = (index - 1) / 3
        val col = (index - 1) % 3
        return if (gameField[row][col] == PlayerSymbol.UNSET) {
            gameField[row][col] = player.symbol
            true
        } else false

    }

    override fun toString(): String {
        var str = ""
        gameField.forEach { row ->
            str += "| ${row[0]} | ${row[1]} | ${row[2]}"
        }
        return str
    }

    fun toFlatList(): List<Int> {
        return gameField.flatten().map { it.fieldToInt() }
    }

}

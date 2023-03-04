package dev.nikitatarasov.model

import io.github.oshai.KotlinLogging
import kotlinx.serialization.Serializable

private val logger = KotlinLogging.logger {}

@Serializable
class GameBoard {

    private var gamefield: List<MutableList<PlayerSymbol>> = List(3) { MutableList(3) { PlayerSymbol.UNSET } }
    private var moves: Int = 0

    fun setSymbol(player: Player, index: Int): Boolean{
        moves++
        val row = (index-1)/3
        val col = (index-1)%3
        return if (gamefield[row][col] == PlayerSymbol.UNSET){
            gamefield[row][col] = player.symbol
            true
        } else false

    }

    fun isBoardFull(): Boolean{
        return moves >= 10
    }

    fun hasGameWinner(firstPlayer: Player, secondPlayer: Player): Player?{
        if (moves > 4){ // min 5 moves
            var winningSymbol: PlayerSymbol? = null

            // |x|x|x|
            // | | | |
            // | | | |
            if ( gamefield[0][0].equalsAll(gamefield[0][1], gamefield[0][2]) ) winningSymbol = gamefield[0][0]

            // | | | |
            // |x|x|x|
            // | | | |
            else if ( gamefield[1][0].equalsAll(gamefield[1][1], gamefield[1][2]) ) winningSymbol = gamefield[1][0]

            // | | | |
            // | | | |
            // |x|x|x|
            else if ( gamefield[2][0].equalsAll(gamefield[2][1], gamefield[2][2]) ) winningSymbol = gamefield[2][0]

            // |x| | |
            // |x| | |
            // |x| | |
            else if ( gamefield[0][0].equalsAll(gamefield[1][0], gamefield[2][0]) ) winningSymbol = gamefield[0][0]

            // | |x| |
            // | |x| |
            // | |x| |
            else if ( gamefield[0][1].equalsAll(gamefield[1][1], gamefield[2][1]) ) winningSymbol = gamefield[0][1]

            // | | |x|
            // | | |x|
            // | | |x|
            else if ( gamefield[0][2].equalsAll(gamefield[1][2], gamefield[2][2]) ) winningSymbol = gamefield[0][2]

            // |x| | |
            // | |x| |
            // | | |x|
            else if ( gamefield[0][0].equalsAll(gamefield[1][1], gamefield[2][2]) ) winningSymbol = gamefield[0][0]

            // | | |x|
            // | |x| |
            // |x| | |
            else if ( gamefield[0][2].equalsAll(gamefield[1][1], gamefield[2][0]) ) winningSymbol = gamefield[0][2]

            logger.info("symbol " + winningSymbol?.fieldToSymbol())
            return if (winningSymbol == null) null
            else if (firstPlayer.symbol == winningSymbol) firstPlayer
            else if (secondPlayer.symbol == winningSymbol) secondPlayer
            else null
        }
        return null
    }

    override fun toString(): String {
        var str = ""
        gamefield.forEach { row ->
            str += "| ${row[0]} | ${row[1]} | ${row[2]}"
        }
        return str
    }

    fun toJSON(): String {
        return """
            {
                "firstRow": [
                    first: ${gamefield[0][0].fieldToInt()},
                    second: ${gamefield[0][1].fieldToInt()},
                    third: ${gamefield[0][2].fieldToInt()}
                ],
                "secondRow": [
                    first: ${gamefield[1][0].fieldToInt()},
                    second: ${gamefield[1][1].fieldToInt()},
                    third: ${gamefield[1][2].fieldToInt()}
                ],
                "thirdRow": [
                    first: ${gamefield[2][0].fieldToInt()},
                    second: ${gamefield[2][1].fieldToInt()},
                    third: ${gamefield[2][2].fieldToInt()} 
                ],
            }
        """.trimIndent()
    }

    fun toJSONList(): String {
        return """
            [ ${gamefield[0][0].fieldToInt()}, ${gamefield[0][1].fieldToInt()}, ${gamefield[0][2].fieldToInt()},
              ${gamefield[1][0].fieldToInt()}, ${gamefield[1][1].fieldToInt()}, ${gamefield[1][2].fieldToInt()},
              ${gamefield[2][0].fieldToInt()}, ${gamefield[2][1].fieldToInt()}, ${gamefield[2][2].fieldToInt()} 
            ]
        """.trimIndent()
    }

}

private fun PlayerSymbol.equalsAll(vararg values: PlayerSymbol): Boolean{
    return values.asList().all { it.fieldToSymbol() == this.fieldToSymbol() }
}

package dev.nikitatarasov.model

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
            else if ( gamefield[1][0].equalsAll(gamefield[1][0], gamefield[2][0]) ) winningSymbol = gamefield[1][0]

            // | | |x|
            // | | |x|
            // | | |x|
            else if ( gamefield[2][0].equalsAll(gamefield[1][0], gamefield[2][0]) ) winningSymbol = gamefield[2][0]

            // |x| | |
            // | |x| |
            // | | |x|
            else if ( gamefield[0][0].equalsAll(gamefield[1][1], gamefield[2][2]) ) winningSymbol = gamefield[0][0]

            // | | |x|
            // | |x| |
            // |x| | |
            else if ( gamefield[0][2].equalsAll(gamefield[1][1], gamefield[2][0]) ) winningSymbol = gamefield[0][2]

            print("symbol " + winningSymbol?.fieldToSymbol())
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
                "one": "${gamefield[0][0].fieldToSymbol()}",
                "two": "${gamefield[0][1].fieldToSymbol()}",
                "three": "${gamefield[0][2].fieldToSymbol()}",
                "four": "${gamefield[1][0].fieldToSymbol()}",
                "five": "${gamefield[1][1].fieldToSymbol()}",
                "six": "${gamefield[1][2].fieldToSymbol()}",
                "seven": "${gamefield[2][0].fieldToSymbol()}",
                "eight": "${gamefield[2][1].fieldToSymbol()}",
                "nine": "${gamefield[2][2].fieldToSymbol()}"
            }
        """.trimIndent()
    }

}

private fun PlayerSymbol.equalsAll(vararg values: PlayerSymbol): Boolean{
    return values.asList().all { it.fieldToSymbol() == this.fieldToSymbol() }
}
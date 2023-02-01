package dev.nikitatarasov.model

class GameBoard {

    private var gamefield: List<MutableList<PlayerSymbol>> = List(3) { MutableList(3) { PlayerSymbol.UNSET } }

    fun setSymbol(player: Player, index: Int){
        val row = (index-1)/3
        val col = (index-1)%3
        gamefield[row][col] = player.symbol
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

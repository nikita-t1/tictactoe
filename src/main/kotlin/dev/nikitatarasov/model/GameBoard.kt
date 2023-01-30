package dev.nikitatarasov.model

class GameBoard {

    private var gamefield: List<MutableList<PlayerSymbol>> = List(3) { MutableList(3) { PlayerSymbol.UNSET } }

    override fun toString(): String {
        var str = ""
        gamefield.forEach { row ->
            str += "| ${row[0]} | ${row[1]} | ${row[2]}"
        }
        return str
    }


}

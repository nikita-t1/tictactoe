package dev.nikitatarasov.util

import dev.nikitatarasov.model.GameBoard
import dev.nikitatarasov.model.PlayerSymbol
import io.github.oshai.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Utility class for game board operations.
 *
 * Bundles all functions related to game board operations, that DON'T mutate the game board state.
 */
object GameBoardUtils {

    fun GameBoard.isBoardFull(): Boolean {
        val moves = gameField.flatten().count { it != PlayerSymbol.UNSET }
        return moves >= 9
    }

    fun checkDraw(gameBoard: GameBoard): Boolean {
        return gameBoard.isBoardFull() && checkForWinningSymbol(gameBoard) == null
    }

    fun checkWinner(gameBoard: GameBoard): PlayerSymbol? {
        return checkForWinningSymbol(gameBoard)
    }

    fun checkGameOver(gameBoard: GameBoard): Boolean {
        return checkDraw(gameBoard) || checkWinner(gameBoard) != null
    }

    private fun checkForWinningSymbol(gameBoard: GameBoard): PlayerSymbol? {
        val moves = gameBoard.gameField.flatten().count { it != PlayerSymbol.UNSET }
        val gamefield = gameBoard.gameField
        if (moves > 4) { // min 5 moves
            var winningSymbol: PlayerSymbol? = null

            // |x|x|x|
            // | | | |
            // | | | |
            if (gamefield[0][0].equalsAll(gamefield[0][1], gamefield[0][2]) && gamefield[0][0] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[0][0]
            }

            // | | | |
            // |x|x|x|
            // | | | |
            else if (gamefield[1][0].equalsAll(gamefield[1][1], gamefield[1][2]) && gamefield[1][0] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[1][0]
            }

            // | | | |
            // | | | |
            // |x|x|x|
            else if (gamefield[2][0].equalsAll(gamefield[2][1], gamefield[2][2]) && gamefield[2][0] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[2][0]
            }

            // |x| | |
            // |x| | |
            // |x| | |
            else if (gamefield[0][0].equalsAll(gamefield[1][0], gamefield[2][0]) && gamefield[0][0] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[0][0]
            }

            // | |x| |
            // | |x| |
            // | |x| |
            else if (gamefield[0][1].equalsAll(gamefield[1][1], gamefield[2][1]) && gamefield[0][1] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[0][1]
            }

            // | | |x|
            // | | |x|
            // | | |x|
            else if (gamefield[0][2].equalsAll(gamefield[1][2], gamefield[2][2]) && gamefield[0][2] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[0][2]
            }

            // |x| | |
            // | |x| |
            // | | |x|
            else if (gamefield[0][0].equalsAll(gamefield[1][1], gamefield[2][2]) && gamefield[0][0] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[0][0]
            }

            // | | |x|
            // | |x| |
            // |x| | |
            else if (gamefield[0][2].equalsAll(gamefield[1][1], gamefield[2][0]) && gamefield[0][2] != PlayerSymbol.UNSET) {
                winningSymbol = gamefield[0][2]
            }

            logger.info("symbol " + winningSymbol?.fieldToSymbol())
            return winningSymbol
        }
        return null
    }

    private fun PlayerSymbol.equalsAll(vararg values: PlayerSymbol): Boolean {
        return values.asList().all { it.fieldToSymbol() == this.fieldToSymbol() }
    }

}

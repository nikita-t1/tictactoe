package dev.nikitatarasov.util

import dev.nikitatarasov.model.GameBoard
import dev.nikitatarasov.model.PlayerSymbol
import io.github.oshai.KotlinLogging

/**
 * Utility class for game board operations.
 *
 * Bundles all functions related to game board operations, that DON'T mutate the game board state.
 */
object GameBoardUtils {

    private val logger = KotlinLogging.logger {}

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
        val gameField = gameBoard.gameField
        if (moves > 4) { // min 5 moves
            var winningSymbol: PlayerSymbol? = null

            // |x|x|x|
            // | | | |
            // | | | |
            if (gameField[0][0].equalsAll(gameField[0][1], gameField[0][2]) && gameField[0][0] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[0][0]
            }

            // | | | |
            // |x|x|x|
            // | | | |
            else if (gameField[1][0].equalsAll(gameField[1][1], gameField[1][2]) && gameField[1][0] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[1][0]
            }

            // | | | |
            // | | | |
            // |x|x|x|
            else if (gameField[2][0].equalsAll(gameField[2][1], gameField[2][2]) && gameField[2][0] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[2][0]
            }

            // |x| | |
            // |x| | |
            // |x| | |
            else if (gameField[0][0].equalsAll(gameField[1][0], gameField[2][0]) && gameField[0][0] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[0][0]
            }

            // | |x| |
            // | |x| |
            // | |x| |
            else if (gameField[0][1].equalsAll(gameField[1][1], gameField[2][1]) && gameField[0][1] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[0][1]
            }

            // | | |x|
            // | | |x|
            // | | |x|
            else if (gameField[0][2].equalsAll(gameField[1][2], gameField[2][2]) && gameField[0][2] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[0][2]
            }

            // |x| | |
            // | |x| |
            // | | |x|
            else if (gameField[0][0].equalsAll(gameField[1][1], gameField[2][2]) && gameField[0][0] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[0][0]
            }

            // | | |x|
            // | |x| |
            // |x| | |
            else if (gameField[0][2].equalsAll(gameField[1][1], gameField[2][0]) && gameField[0][2] != PlayerSymbol.UNSET) {
                winningSymbol = gameField[0][2]
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

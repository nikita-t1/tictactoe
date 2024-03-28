import {EMPTY_BOARD_FIELD, MOVE_BY_OPPONENT as MOVE_BY_COMPUTER , MOVE_BY_PLAYER, winConditions} from "@/helper/GameBoardHelper";

function computerMove(gameBoard: number[]): number[] {
    return computerSmartMove(gameBoard)
}

/**
 * Executes a smart move for the computer player.
 *
 * The method checks if the computer player can win the game by completing one of the win conditions.
 *
 * If the computer player cannot win, it checks if it can block the player from winning.
 *
 * If neither winning nor blocking move is possible, the computer makes a random move by calling the computerMove method.
 */
function computerSmartMove(gameBoard: number[]): number[] {

    // Check if I can win
    for (const condition of winConditions) {
        const [a, b, c] = condition
        if (gameBoard[a] === MOVE_BY_COMPUTER && gameBoard[b] === MOVE_BY_COMPUTER && gameBoard[c] === EMPTY_BOARD_FIELD) {
            gameBoard[c] = MOVE_BY_COMPUTER
            return gameBoard
        }
        if (gameBoard[a] === MOVE_BY_COMPUTER && gameBoard[b] === EMPTY_BOARD_FIELD && gameBoard[c] === MOVE_BY_COMPUTER) {
            gameBoard[b] = MOVE_BY_COMPUTER
            return gameBoard
        }
        if (gameBoard[a] === EMPTY_BOARD_FIELD && gameBoard[b] === MOVE_BY_COMPUTER && gameBoard[c] === MOVE_BY_COMPUTER) {
            gameBoard[a] = MOVE_BY_COMPUTER
            return gameBoard
        }
    }

    // Check if I can block
    for (const condition of winConditions) {
        const [a, b, c] = condition
        if (gameBoard[a] === MOVE_BY_PLAYER && gameBoard[b] === 1 && gameBoard[c] === EMPTY_BOARD_FIELD) {
            gameBoard[c] = MOVE_BY_COMPUTER
            return gameBoard
        }
        if (gameBoard[a] === MOVE_BY_PLAYER && gameBoard[b] === EMPTY_BOARD_FIELD && gameBoard[c] === MOVE_BY_PLAYER) {
            gameBoard[b] = MOVE_BY_COMPUTER
            return gameBoard
        }
        if (gameBoard[a] === EMPTY_BOARD_FIELD && gameBoard[b] === MOVE_BY_PLAYER && gameBoard[c] === MOVE_BY_PLAYER) {
            gameBoard[a] = MOVE_BY_COMPUTER
            return gameBoard
        }
    }

    return computerRandomMove(gameBoard)
}

/**
 * Makes a move for the computer player on the game board.
 *
 * randomly selects an empty field on the game board and sets it to the computer's value (2).
 */
function computerRandomMove(gameBoard: number[]): number[] {

    let index = Math.floor(Math.random() * 9) // Randomly choose field
    while (gameBoard[index] !== EMPTY_BOARD_FIELD) { // Check if field is already set
        index = Math.floor(Math.random() * 9) // Randomly choose field
    }

    gameBoard[index] = MOVE_BY_COMPUTER // Set field
    return gameBoard
}

export default {
    computerMove
}

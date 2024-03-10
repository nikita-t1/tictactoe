export const EMPTY_BOARD_FIELD = 0;
export const MOVE_BY_PLAYER = 1;
export const MOVE_BY_COMPUTER = 2;

/**
 * Represents the win conditions for a game board.
 *
 * The winConditions variable is an array of arrays, where each inner array
 * represents a possible winning combination on the game board.
 *
 * Each inner array consists of three numbers representing the indexes of the
 * game board positions that need to be filled in order to win the game.
 *
 * The win conditions are categorized into three types: horizontal, vertical,
 * and diagonal. Each type has a group of winning combinations.
 */
export const winConditions = [
    [0, 1, 2], // Horizontal
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6], // Vertical
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8], // Diagonal
    [2, 4, 6]
]

/**
 * Checks if the game board is completely filled.
 *
 * @param {number[]} gameBoard - The game board to check.
 * @returns {boolean} - Returns true if the game board is completely filled, otherwise false.
 */
export const checkDraw = (gameBoard: number[]): boolean => {
    for (const field of gameBoard) {
        if (field === 0) return false
    }
    return true

}

/**
 * Checks the winner of the game based on the game board.
 *
 * @param {number[]} gameBoard - The game board represented as an array of numbers.
 * @returns {number} - The winner of the game. It can be one of the following:
 *  - 0: Indicates an empty board field (no winner yet).
 *  - 1: Indicates the player has won the game.
 *  - 2: Indicates the computer/opponent has won the game.
 */
export const checkWinner = (gameBoard: number[]): number => {
    for (const condition of winConditions) {
        if (gameBoard[condition[0]] === 1 && gameBoard[condition[1]] === 1 && gameBoard[condition[2]] === 1) {
            return MOVE_BY_PLAYER
        }
        if (gameBoard[condition[0]] === 2 && gameBoard[condition[1]] === 2 && gameBoard[condition[2]] === 2) {
            return MOVE_BY_COMPUTER
        }
    }
    return EMPTY_BOARD_FIELD
}


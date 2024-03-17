import { test } from "vitest"
import {EMPTY_BOARD_FIELD, MOVE_BY_COMPUTER, MOVE_BY_PLAYER} from "../../src/helper/GameBoardHelper";
import ComputerBrainHelper from "../../src/helper/ComputerBrainHelper";

// replace "path-to-your-code" with the actual file location

test('can compute a "winning move"', ({ expect }) => {
    const gameBoard = [
        MOVE_BY_COMPUTER, MOVE_BY_COMPUTER, EMPTY_BOARD_FIELD,
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD
    ]
    const newBoard = ComputerBrainHelper.computerMove(gameBoard)
    expect(newBoard[2]).toBe(MOVE_BY_COMPUTER)
})

test('can block a "winning move" by the player', ({ expect }) => {
    const gameBoard = [
        EMPTY_BOARD_FIELD, MOVE_BY_PLAYER, MOVE_BY_PLAYER,
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD
    ]
    const newBoard = ComputerBrainHelper.computerMove(gameBoard)
    expect(newBoard[0]).toBe(MOVE_BY_COMPUTER)
})

test('should randomly choose a move when none of the above conditions are met', ({ expect }) => {
    const gameBoard = [
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
        EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD
    ]
    const newBoard = ComputerBrainHelper.computerMove(gameBoard)
    expect(newBoard.includes(MOVE_BY_COMPUTER)).toBe(true)
})

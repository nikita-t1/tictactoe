import { test } from "vitest"
import { EMPTY_BOARD_FIELD, MOVE_BY_PLAYER, MOVE_BY_OPPONENT, winConditions, checkDraw, checkWinner } from "../../src/helper/GameBoardHelper";

test('checking winConditions constant', ({ expect }) => {
    expect(winConditions.length).toBe(8) // As 8 possible win combinations for Tic Tac Toe
})

test('checkDraw correctly identifies draw', ({ expect }) => {
    const fullBoard = [
        MOVE_BY_PLAYER, MOVE_BY_OPPONENT, MOVE_BY_PLAYER,
        MOVE_BY_OPPONENT, MOVE_BY_PLAYER, MOVE_BY_OPPONENT,
        MOVE_BY_PLAYER, MOVE_BY_OPPONENT, MOVE_BY_PLAYER
    ]
    expect(checkDraw(fullBoard)).toBe(true)
})

test('checkDraw correctly identifies not a draw', ({ expect }) => {
    const notFullBoard = [
        MOVE_BY_PLAYER, MOVE_BY_OPPONENT, MOVE_BY_PLAYER,
        MOVE_BY_OPPONENT, MOVE_BY_PLAYER, EMPTY_BOARD_FIELD,
        MOVE_BY_PLAYER, MOVE_BY_OPPONENT, MOVE_BY_PLAYER
    ]
    expect(checkDraw(notFullBoard)).toBe(false)
})

test('checkWinner correctly identifies player as winner', ({ expect }) => {
    const winningPlayerBoard = [
        MOVE_BY_PLAYER, MOVE_BY_PLAYER, MOVE_BY_PLAYER,
        MOVE_BY_OPPONENT, EMPTY_BOARD_FIELD, MOVE_BY_OPPONENT,
        EMPTY_BOARD_FIELD, MOVE_BY_OPPONENT, EMPTY_BOARD_FIELD
    ]
    expect(checkWinner(winningPlayerBoard)).toBe(MOVE_BY_PLAYER)
})

test('checkWinner correctly identifies opponent as winner', ({ expect }) => {
    const winningComputerBoard = [
        MOVE_BY_OPPONENT, MOVE_BY_PLAYER, MOVE_BY_PLAYER,
        MOVE_BY_OPPONENT, MOVE_BY_PLAYER, MOVE_BY_PLAYER,
        MOVE_BY_OPPONENT, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD
    ]
    expect(checkWinner(winningComputerBoard)).toBe(MOVE_BY_OPPONENT)
})

test('checkWinner correctly identifies a draw', ({ expect }) => {
    const drawBoard = [
        MOVE_BY_PLAYER, MOVE_BY_OPPONENT, MOVE_BY_PLAYER,
        MOVE_BY_OPPONENT, MOVE_BY_OPPONENT, MOVE_BY_PLAYER,
        MOVE_BY_PLAYER, MOVE_BY_PLAYER, MOVE_BY_OPPONENT
    ]
    expect(checkWinner(drawBoard)).toBe(EMPTY_BOARD_FIELD)
})

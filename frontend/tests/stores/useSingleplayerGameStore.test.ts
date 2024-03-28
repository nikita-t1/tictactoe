import {createPinia, setActivePinia} from 'pinia'
import {EMPTY_BOARD_FIELD, MOVE_BY_OPPONENT, MOVE_BY_PLAYER} from "../../src/helper/GameBoardHelper";
import {WebSocketCodes} from "../../src/StatusCodes";
import {useSingleplayerGameStore} from "../../src/stores/useSingleplayerGameStore";
import {beforeEach, describe, expect, it} from 'vitest'

describe('gameStore', () => {
    let gameStore

    beforeEach(() => {
        // create an instance of pinia and use it for every test
        setActivePinia(createPinia())
        gameStore = useSingleplayerGameStore()
        gameStore.replay()
    })

    it('should initialize game board correctly', () => {
        gameStore.replay()
        expect(gameStore.gameBoard.every((item) => item === 0)).toBe(true)
        expect(gameStore.hasGameEnded).toBe(false)
        expect(gameStore.awaitingMoveBy).not.toBe(EMPTY_BOARD_FIELD)
    })

    it('move the player piece', () => {
        // override the awaitingMoveBy value to simulate the player's turn
        gameStore.$patch({awaitingMoveBy: MOVE_BY_PLAYER})
        gameStore.playerMove(0)
        expect(gameStore.gameBoard[0]).toBe(MOVE_BY_PLAYER)
        expect(gameStore.hasGameEnded).toBe(false)
        expect(gameStore.awaitingMoveBy).toBe(MOVE_BY_OPPONENT)
    })

    it('computer move', () => {
        // override the awaitingMoveBy value to simulate the computer's turn
        gameStore.$patch({awaitingMoveBy: MOVE_BY_OPPONENT})
        gameStore.computerMove()
        expect(gameStore.gameBoard.includes(MOVE_BY_OPPONENT)).toBe(true)
        expect(gameStore.hasGameEnded).toBe(false)
        expect(gameStore.awaitingMoveBy).toBe(MOVE_BY_PLAYER)
    })

    it('check for draw', () => {
        gameStore.gameBoard = [
            EMPTY_BOARD_FIELD, MOVE_BY_OPPONENT, MOVE_BY_PLAYER,
            MOVE_BY_OPPONENT, MOVE_BY_PLAYER, MOVE_BY_OPPONENT,
            MOVE_BY_OPPONENT, MOVE_BY_PLAYER, MOVE_BY_OPPONENT
        ]
        // override the awaitingMoveBy value to simulate the player's turn
        gameStore.$patch({awaitingMoveBy: MOVE_BY_PLAYER})
        gameStore.playerMove(0)
        expect(gameStore.hasGameEnded).toBe(true)
        expect(gameStore.userMessage).toBe("game_draw")
    })

    it('check for winner', () => {
        gameStore.gameBoard = [
            EMPTY_BOARD_FIELD, MOVE_BY_PLAYER, MOVE_BY_PLAYER,
            EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD,
            EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD, EMPTY_BOARD_FIELD
        ]
        // override the awaitingMoveBy value to simulate the player's turn
        gameStore.$patch({awaitingMoveBy: MOVE_BY_PLAYER})
        gameStore.playerMove(0)
        expect(gameStore.hasGameEnded).toBe(true)
        expect(gameStore.userMessage).toBe("you_won")
    })
})

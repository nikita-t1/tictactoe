import { defineStore } from 'pinia'
import { ref } from 'vue'
import { WebSocketCodes } from "@/StatusCodes"
import {default as ComputerBrainHelper} from "@/helper/ComputerBrainHelper";
import {
    checkDraw,
    checkWinner,
    EMPTY_BOARD_FIELD,
    MOVE_BY_OPPONENT,
    MOVE_BY_PLAYER,
    winConditions
} from "@/helper/GameBoardHelper";


export const useSingleplayerGameStore = defineStore('singleplayerGameStore', () => {

    const awaitingMoveBy = ref(EMPTY_BOARD_FIELD)
    // const currentStatusCode = ref<null | WebSocketCodes>(null)
    const userMessage = ref('')
    const gameBoardFields = ref(Array(9).fill(EMPTY_BOARD_FIELD))
    const hasGameEnded = ref(false)

    /**
     * Initializes the game board by setting the game state and triggering the first move.
     */
    function initBoard() {
        hasGameEnded.value = false
        gameBoardFields.value = Array(9).fill(EMPTY_BOARD_FIELD)

        const isPlayerMove = Math.random() < 0.5
        awaitingMoveBy.value = isPlayerMove ? MOVE_BY_PLAYER : MOVE_BY_OPPONENT
        updateStatusAndTriggerNextMove(awaitingMoveBy.value)
    }

    /**
     * Updates the status code and triggers the next move based on the player's turn.
     *
     * @param {number} awaitingMoveBy - The value representing who is expected to make the next move.
     */
    function updateStatusAndTriggerNextMove(awaitingMoveBy: number) {
        const isPlayerTurn = (awaitingMoveBy === MOVE_BY_PLAYER)
        userMessage.value = isPlayerTurn ? 'your_turn' : 'opponents_turn'
        if (!isPlayerTurn) {
            setTimeout(() => {
                computerMove()
            }, 1500)
        }
    }

    /**
     * Initializes the board for a replay.
     */
    function replay() {
        initBoard()
    }

    /**
     * Executes the computer's move in the game.
     */
    function computerMove() {
        const updatedGameBoardFields = ComputerBrainHelper.computerMove(gameBoardFields.value)
        gameBoardFields.value = updatedGameBoardFields

        awaitingMoveBy.value = MOVE_BY_PLAYER
        handleGameBoardChange()
    }

    /**
     * Moves the player's piece to the specified index on the game board.
     *
     * @param {number} index - The index of the field to be set
     */
    function playerMove(index: number) {
        if (awaitingMoveBy.value !== MOVE_BY_PLAYER) return
        if (gameBoardFields.value[index] !== 0) return
        gameBoardFields.value[index] = MOVE_BY_PLAYER
        awaitingMoveBy.value = MOVE_BY_OPPONENT
        handleGameBoardChange()
    }

    /**
     * Handles the change in the game board.
     *
     * Determines if the game has ended in a draw, if a player has won, or if the game can continue.
     * Updates the status code and triggers the next move.
     */
    function handleGameBoardChange() {
        const winner = checkWinner(gameBoardFields.value)
        if (winner !== EMPTY_BOARD_FIELD) {
            userMessage.value = (winner === MOVE_BY_PLAYER ? 'you_won' : 'you_lost')
            hasGameEnded.value = true
            return
        }

        if (checkDraw(gameBoardFields.value)) {
            userMessage.value = 'game_draw'
            hasGameEnded.value = true
            return
        }

        const emptyBoard = gameBoardFields.value.every((field) => field === EMPTY_BOARD_FIELD)
        if (emptyBoard) {
            hasGameEnded.value = true
            return
        }

        updateStatusAndTriggerNextMove(awaitingMoveBy.value)
    }


    return {
        awaitingMoveBy,
        userMessage,
        gameBoard: gameBoardFields,
        hasGameEnded,
        winConditions,
        replay,
        computerMove,
        playerMove,
        checkDraw,
        checkWinner
    }
})

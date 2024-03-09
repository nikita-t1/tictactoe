<template>
    <div class="transition-all duration-700 flex flex-col items-center justify-center mx-auto">

        <!-- Game board -->
        <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
            <div class="grid gap-x-4 gap-y-4 grid-cols-3">
                <div v-for="(item, index) in gameBoard" :key="index" class="field cursor-pointer"
                     :class="{'cursor-not-allowed' : awaitingMoveBy != GameBoard.MOVE_BY_PLAYER}" @click="playerMove(index+1)">
                    <IconCross v-if="item === GameBoard.MOVE_BY_PLAYER"/>
                    <IconCircle v-if="item === GameBoard.MOVE_BY_COMPUTER"/>
                </div>
            </div>
        </div>

        <!-- Status message -->
        <div class="transition-all duration-700 w-96 mt-4 p-2 text-center text-white">
            <span :class="currentStatusCode != null ? 'visible opacity-100' : 'invisible opacity-0'"
                  class="transition-all duration-700 text-gray-800 dark:text-white">
            {{ t("ws_msg." + currentStatusCode) }} <!-- "ws_msg.4302" -->
         </span>
        </div>

        <!-- Replay and Home buttons -->
        <div v-if="hasGameEnded"
             class="transition-all duration-700 flex flex-col md:flex-row max-w-xl content-center self-center items-center justify-center mx-auto space-x-2">
            <div
                class="flex-none mt-10 mx-auto inline-flex items-center gap-2 text-sm font-medium text-blue-500 hover:text-blue-700">
                <button type="button" @click="replay"
                        class="transition-all duration-700 py-[.688rem] px-4 mx-auto w-48 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-600 hover:bg-blue-700 text-white text-sm dark:focus:ring-offset-gray-800">
                    {{ t("replay") }}
                </button>
            </div>
            <router-link @click="replay" to="/"
                         class="flex-none mx-auto inline-flex items-center gap-2 mt-10  text-sm font-medium text-blue-500 hover:text-blue-700">
                <button type="button"
                        class="transition-all duration-700 py-[.688rem] px-4 w-48 inline-flex justify-center items-center gap-2 rounded-md border-2 border-gray-200 font-semibold text-blue-600 hover:text-white hover:bg-blue-600 hover:border-blue-600 text-sm dark:border-gray-700 dark:hover:border-blue-600">
                    {{ t("home") }}
                </button>
            </router-link>

        </div>
    </div>


</template>

<script setup lang="ts">

import {computed, defineComponent, reactive, type Ref} from 'vue'
import {onMounted, ref, watch} from "vue";
import {WebSocketCodes} from "@/StatusCodes";
import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";
import {useI18n} from "vue-i18n";
import GameBoard from "@/service/GameBoard";

const {t} = useI18n()

const currentStatusCode: Ref<null | WebSocketCodes> = ref(null)

const EMPTY_BOARD_FIELD = 0;
const MOVE_BY_PLAYER = 1;
const MOVE_BY_COMPUTER = 2;
const awaitingMoveBy = ref<number>(EMPTY_BOARD_FIELD) // 0: empty, 1: player, 2: computer

const gameBoard: Ref<number[]> = ref([])
gameBoard.value = Array(9).fill(EMPTY_BOARD_FIELD) // fill with 0 (empty board)

const hasGameEnded = ref(false)

watch(gameBoard, (newValue) => {
    if (checkDraw()) {
        currentStatusCode.value = WebSocketCodes.GAME_ENDED_IN_DRAW
        hasGameEnded.value = true
        return
    }
    const winner = checkWinner()
    if (winner !== EMPTY_BOARD_FIELD) {
        currentStatusCode.value = (winner === MOVE_BY_PLAYER ? WebSocketCodes.YOU_WON : WebSocketCodes.OPPONENT_WON)
        hasGameEnded.value = true
        return
    }
    const emptyBoard = newValue.every((field) => field === 0)
    if (emptyBoard) {
        hasGameEnded.value = true
        return
    }
    updateStatusAndTriggerNextMove(awaitingMoveBy.value)
}, {deep: true})

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
const winConditions = [
    [0, 1, 2], // Horizontal
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6], // Vertical
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8], // Diagonal
    [2, 4, 6]
]

onMounted(() => replay())

/**
 * Updates the game status and triggers the next move based on the player's turn.
 *
 * The method is called after each move to update the game status.
 * The method also triggers the computer's move if it is the computer's turn to play.
 *
 * @param {number} awaitingMoveBy - The player ID (1 for player, 2 for computer) who is expected to make the next move.
 *
 */
function updateStatusAndTriggerNextMove(awaitingMoveBy: number) {
    const isPlayerTurn = (awaitingMoveBy === MOVE_BY_PLAYER)
    currentStatusCode.value = isPlayerTurn ? WebSocketCodes.YOUR_MOVE : WebSocketCodes.OPPONENT_MOVE;
    if (!isPlayerTurn) {
        setTimeout(() => {
            computerSmartMove()
        }, 1500)
    }
}

/**
 * Resets the game board and updates the status for the next move.
 *
 * The method is called when the game ends and the player wants to play again.
 */
function replay() {
    gameBoard.value = Array(9).fill(EMPTY_BOARD_FIELD)

    const isPlayerMove = Math.random() < 0.5 // Randomly decide who starts
    awaitingMoveBy.value = isPlayerMove ? MOVE_BY_PLAYER : MOVE_BY_COMPUTER
    updateStatusAndTriggerNextMove(awaitingMoveBy.value)
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
function computerSmartMove() {

    // Check if I can win
    for (const condition of winConditions) {
        const [a, b, c] = condition
        if (gameBoard.value[a] === MOVE_BY_COMPUTER && gameBoard.value[b] === MOVE_BY_COMPUTER && gameBoard.value[c] === EMPTY_BOARD_FIELD) {
            gameBoard.value[c] = MOVE_BY_COMPUTER
            awaitingMoveBy.value = MOVE_BY_PLAYER
            return
        }
        if (gameBoard.value[a] === MOVE_BY_COMPUTER && gameBoard.value[b] === EMPTY_BOARD_FIELD && gameBoard.value[c] === MOVE_BY_COMPUTER) {
            gameBoard.value[b] = MOVE_BY_COMPUTER
            awaitingMoveBy.value = MOVE_BY_PLAYER
            return
        }
        if (gameBoard.value[a] === EMPTY_BOARD_FIELD && gameBoard.value[b] === MOVE_BY_COMPUTER && gameBoard.value[c] === MOVE_BY_COMPUTER) {
            gameBoard.value[a] = MOVE_BY_COMPUTER
            awaitingMoveBy.value = MOVE_BY_PLAYER
            return
        }
    }

    // Check if I can block
    for (const condition of winConditions) {
        const [a, b, c] = condition
        if (gameBoard.value[a] === MOVE_BY_PLAYER && gameBoard.value[b] === 1 && gameBoard.value[c] === EMPTY_BOARD_FIELD) {
            gameBoard.value[c] = MOVE_BY_COMPUTER
            awaitingMoveBy.value = MOVE_BY_PLAYER
            return
        }
        if (gameBoard.value[a] === MOVE_BY_PLAYER && gameBoard.value[b] === EMPTY_BOARD_FIELD && gameBoard.value[c] === MOVE_BY_PLAYER) {
            gameBoard.value[b] = MOVE_BY_COMPUTER
            awaitingMoveBy.value = MOVE_BY_PLAYER
            return
        }
        if (gameBoard.value[a] === EMPTY_BOARD_FIELD && gameBoard.value[b] === MOVE_BY_PLAYER && gameBoard.value[c] === MOVE_BY_PLAYER) {
            gameBoard.value[a] = MOVE_BY_COMPUTER
            awaitingMoveBy.value = MOVE_BY_PLAYER
            return
        }
    }

    computerMove()
}

/**
 * Makes a move for the computer player on the game board.
 *
 * randomly selects an empty field on the game board and sets it to the computer's value (2).
 */
function computerMove() {

    let index = Math.floor(Math.random() * 9) // Randomly choose field
    while (gameBoard.value[index] !== EMPTY_BOARD_FIELD) { // Check if field is already set
        index = Math.floor(Math.random() * 9) // Randomly choose field
    }

    gameBoard.value[index] = MOVE_BY_COMPUTER // Set field
    awaitingMoveBy.value = MOVE_BY_PLAYER
}

/**
 * Updates the game state with a move made by the player.
 *
 * @param {number} index - The index of the field to be set (1-based index)
 */
function playerMove(index: number) {
    if (awaitingMoveBy.value !== MOVE_BY_PLAYER) return // Not my move
    if (gameBoard.value[index - 1] !== 0) return // Field already set

    gameBoard.value[index - 1] = 1 // Set field
    awaitingMoveBy.value = MOVE_BY_COMPUTER
}

/**
 * Checks if the game ended in a draw.
 *
 * @returns {boolean} - Returns true if the game ended in a draw, otherwise returns false.
 */
function checkDraw(): boolean {
    for (const field of gameBoard.value) {
        if (field === 0) return false
    }
    return true
}

/**
 * Checks if there is a winner on the game board.
 *
 * @returns {number} - The player ID (1 for player, 2 for computer) who won the game. Returns 0 if there is no winner.
 */
function checkWinner(): number {
    for (const condition of winConditions) {
        if (gameBoard.value[condition[0]] === 1 && gameBoard.value[condition[1]] === 1 && gameBoard.value[condition[2]] === 1) {
            return MOVE_BY_PLAYER
        }
        if (gameBoard.value[condition[0]] === 2 && gameBoard.value[condition[1]] === 2 && gameBoard.value[condition[2]] === 2) {
            return MOVE_BY_COMPUTER
        }
    }
    return EMPTY_BOARD_FIELD
}

</script>

<style scoped>
.field {
    @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>

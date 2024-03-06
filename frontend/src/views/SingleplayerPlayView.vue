<template>
    <div class="transition-all duration-700 flex flex-col items-center justify-center mx-auto">

        <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
            <div class="grid gap-x-4 gap-y-4 grid-cols-3">
                <div v-for="(item, index) in gameBoard" class="field cursor-pointer" :class="{'cursor-not-allowed' : !isPlayerMove}" @click="playerMove(index+1)">
                    <IconCross v-if="item === 1"/>
                    <IconCircle v-if="item === 2"/>
                </div>
            </div>
        </div>

        <div class="transition-all duration-700 w-96 mt-4 p-2 text-center text-white">

        <span :class="currentStatusCode != null ? 'visible opacity-100' : 'invisible opacity-0'"
              class="transition-all duration-700 text-gray-800 dark:text-white">
            {{ t("ws_msg." + currentStatusCode) }} <!-- "ws_msg.4302" -->
         </span>
        </div>

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

import type {Ref} from 'vue'
import {onMounted, ref, watch} from "vue";
import {WebSocketCodes} from "@/StatusCodes";
import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";
import {useI18n} from "vue-i18n";

const {t} = useI18n()

const currentStatusCode: Ref<null | string> = ref(null)

const isPlayerMove = ref(false)
const gameBoard: Ref<number[]> = ref([0, 0, 0, 0, 0, 0, 0, 0, 0])
const hasGameEnded = ref(false)

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

watch(isPlayerMove, (newValue) => {
    if (checkWin()) return // Game already ended
    if (checkDraw()) return // Game already ended
    if (!newValue) { // Computer move
        currentStatusCode.value = WebSocketCodes.OPPONENT_MOVE.toString()
        setTimeout(() => {
            computerSmartMove()
        }, 1000)
    } else { // My move
        currentStatusCode.value = WebSocketCodes.YOUR_MOVE.toString()
    }
})

function replay() {
    gameBoard.value = [0, 0, 0, 0, 0, 0, 0, 0, 0]
    hasGameEnded.value = false
    isPlayerMove.value = Math.random() < 0.5 // Randomly decide who starts
    if (!isPlayerMove.value) {
        setTimeout(() => {
            computerSmartMove()
        }, 1500)
    }
}

function computerSmartMove() {

    // Check if I can win
    for (const condition of winConditions) {
        const [a, b, c] = condition
        if (gameBoard.value[a] === 2 && gameBoard.value[b] === 2 && gameBoard.value[c] === 0) {
            gameBoard.value[c] = 2
            isPlayerMove.value = true
            return
        }
        if (gameBoard.value[a] === 2 && gameBoard.value[b] === 0 && gameBoard.value[c] === 2) {
            gameBoard.value[b] = 2
            isPlayerMove.value = true
            return
        }
        if (gameBoard.value[a] === 0 && gameBoard.value[b] === 2 && gameBoard.value[c] === 2) {
            gameBoard.value[a] = 2
            isPlayerMove.value = true
            return
        }
    }

    // Check if I can block
    for (const condition of winConditions) {
        const [a, b, c] = condition
        if (gameBoard.value[a] === 1 && gameBoard.value[b] === 1 && gameBoard.value[c] === 0) {
            gameBoard.value[c] = 2
            isPlayerMove.value = true
            return
        }
        if (gameBoard.value[a] === 1 && gameBoard.value[b] === 0 && gameBoard.value[c] === 1) {
            gameBoard.value[b] = 2
            isPlayerMove.value = true
            return
        }
        if (gameBoard.value[a] === 0 && gameBoard.value[b] === 1 && gameBoard.value[c] === 1) {
            gameBoard.value[a] = 2
            isPlayerMove.value = true
            return
        }
    }

    computerMove()
}

function computerMove() {

    let index = Math.floor(Math.random() * 9) // Randomly choose field
    while (gameBoard.value[index] !== 0) { // Check if field is already set
        index = Math.floor(Math.random() * 9) // Randomly choose field
    }

    gameBoard.value[index] = 2 // Set field
    isPlayerMove.value = true // Enable my move
}

function playerMove(index: number) {
    if (!isPlayerMove.value) return // Not my move
    if (gameBoard.value[index - 1] !== 0) return // Field already set

    gameBoard.value[index - 1] = 1 // Set field
    isPlayerMove.value = false // Disable my move
}

function checkDraw() {
    for (const field of gameBoard.value) {
        if (field === 0) return false
    }

    hasGameEnded.value = true
    currentStatusCode.value = WebSocketCodes.GAME_ENDED_IN_DRAW.toString()
    return true
}

function checkWin() {

    for (const condition of winConditions) {
        if (gameBoard.value[condition[0]] === 1 && gameBoard.value[condition[1]] === 1 && gameBoard.value[condition[2]] === 1) {
            currentStatusCode.value = WebSocketCodes.YOU_WON.toString()
            hasGameEnded.value = true
            return true
        } else if (gameBoard.value[condition[0]] === 2 && gameBoard.value[condition[1]] === 2 && gameBoard.value[condition[2]] === 2) {
            currentStatusCode.value = WebSocketCodes.OPPONENT_WON.toString()
            hasGameEnded.value = true
            return true
        }
    }
    return false
}

</script>

<style scoped>
.field {
    @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>

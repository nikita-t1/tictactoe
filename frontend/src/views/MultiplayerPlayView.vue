<template>
    <div class="flex flex-col items-center justify-center mx-auto">
        <OpponentDisconnectedModal />

        <GameBoard :gameBoard="gameBoard" :awaiting-move-by="awaitingMoveBy"
                   @player-move="(index) => playerMove(index)"/>

        <div class="w-96 mt-4 p-2 text-center">
        <span :class="currentStatusCode != null ? 'visible opacity-100' : 'invisible opacity-0'">
            {{ t(userMessage) }}
         </span>
        </div>

        <div v-if="hasGameEnded"
             class="flex flex-col md:flex-row max-w-xl content-center self-center items-center justify-center mx-auto space-x-2">
            <div
                class="flex-none mt-10 mx-auto inline-flex items-center gap-2 text-sm font-medium">
                <button type="button" @click="webSocketStore.requestRematch()"
                        class="btn btn-primary w-full">
                    {{ t(rematchText) }}
                </button>
            </div>
            <router-link @click="webSocketStore.reset()" to="/"
                         class="flex-none mx-auto inline-flex items-center gap-2 mt-10  text-sm font-medium">
                <button type="button"
                        class="btn btn-ghost w-full">
                    {{ t("home") }}
                </button>
            </router-link>

        </div>
    </div>


</template>

<script setup lang="ts">

import {useWebSocketStore} from "@/stores/websocketStore";
import {computed, onMounted, watch} from "vue";
import {ErrorCodes} from "@/StatusCodes";
import OpponentDisconnectedModal from "@/components/OpponentDisconnectedModal.vue";
import {useI18n} from "vue-i18n";
import {onBeforeRouteLeave, useRouter} from "vue-router";
import {storeToRefs} from "pinia";
import GameBoard from "@/components/GameBoard.vue";
import {MOVE_BY_OPPONENT, MOVE_BY_PLAYER} from "@/helper/GameBoardHelper";

const {t} = useI18n()
const router = useRouter()

const webSocketStore = useWebSocketStore()
const {ws,gameCode, gameBoard, hasGameEnded, isMyMove, rematchText, currentStatusCode,userMessage} = storeToRefs(webSocketStore)
const awaitingMoveBy = computed(() => isMyMove ? MOVE_BY_PLAYER : MOVE_BY_OPPONENT)

/**
 * Change the URL if the gamaCode changes
 *
 * This will happen when a rematch request was accepted and the players are redirected to the new game
 */
watch(gameCode, (value) => {
    if (value != null) {
        router.push({path: "/multiplayer/play", query: {gameCode: value}})
    }
})

function playerMove(index: number) {
    console.log(`playerMove(${index+1})`)
    webSocketStore.sendGameBoardMove(index+1)
}

onMounted(() => {
    console.log(ws.value?.url)
    console.log(ws.value)
    if (ws.value == null) {
        router.push({path: "/error", hash: `#${ErrorCodes.WEBSOCKET_IS_NULL}`})
    }
})

onBeforeRouteLeave(() => {
    webSocketStore.reset()
})


</script>

<style scoped>
.field {
    @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>

<template>
    <div class="transition-all duration-700 flex flex-col items-center justify-center mx-auto">
        <OpponentDisconnectedModal ref="OpponentDisconnectedModalRef"/>

        <GameBoard :gameBoard="gameBoard" :awaiting-move-by="awaitingMoveBy"
                   @player-move="(index) => playerMove(index)"/>

        <div class="transition-all duration-700 w-96 mt-4 p-2 text-center text-white">
        <span :class="currentStatusCode != null ? 'visible opacity-100' : 'invisible opacity-0'"
              class="transition-all duration-700 text-gray-800 dark:text-white">
            {{ t(userMessage) }} <!-- "ws_msg.4302" -->
         </span>
        </div>

        <div v-if="hasGameEnded"
             class="transition-all duration-700 flex flex-col md:flex-row max-w-xl content-center self-center items-center justify-center mx-auto space-x-2">
            <div
                class="flex-none mt-10 mx-auto inline-flex items-center gap-2 text-sm font-medium text-blue-500 hover:text-blue-700">
                <button type="button" @click="webSocketStore.requestRematch()"
                        class="transition-all duration-700 py-[.688rem] px-4 mx-auto w-48 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-600 hover:bg-blue-700 text-white text-sm dark:focus:ring-offset-gray-800">
                    {{ rematchRequested ? t(userMessage) : t("request_rematch") }}
                </button>
            </div>
            <router-link @click="webSocketStore.reset()" to="/"
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

import {useWebSocketStore} from "@/stores/websocketStore";
import {computed, onMounted, ref, watch} from "vue";
import {ErrorCodes, WebSocketCodes} from "@/StatusCodes";
import OpponentDisconnectedModal from "@/components/OpponentDisconnectedModal.vue";
import {useI18n} from "vue-i18n";
import {onBeforeRouteLeave, useRouter} from "vue-router";
import {storeToRefs} from "pinia";
import GameBoard from "@/components/GameBoard.vue";
import {MOVE_BY_OPPONENT, MOVE_BY_PLAYER} from "@/helper/GameBoardHelper";

const {t} = useI18n()
const router = useRouter()

const webSocketStore = useWebSocketStore()
const {ws, gameBoard, hasGameEnded, isMyMove, rematchRequested, currentStatusCode, bothPlayersConnected,userMessage} = storeToRefs(webSocketStore)
const awaitingMoveBy = computed(() => isMyMove ? MOVE_BY_PLAYER : MOVE_BY_OPPONENT)

const OpponentDisconnectedModalRef = ref<InstanceType<typeof OpponentDisconnectedModal> | null>(null)

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

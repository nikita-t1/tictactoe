<template>
    <div class="transition-all duration-700 flex flex-col h-screen items-center justify-center mx-auto">
        <OpponentDisconnectedModal ref="OpponentDisconnectedModalRef" @timeout-reached.once="timeoutReached"/>

        <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
            <div class="grid gap-x-4 gap-y-4 grid-cols-3">
                <div v-for="(item, index) in gameBoard" class="field cursor-pointer"
                     :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(index+1)">
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
                class="flex-none mt-10 mx-auto inline-flex items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
                <button type="button" @click="requestRematch"
                        class="transition-all duration-700 py-[.688rem] px-4 mx-auto w-48 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-600 hover:bg-blue-700 text-white text-sm dark:focus:ring-offset-gray-800">
                    {{ rematchRequestAccept ? t("ws_msg." + WebSocketCodes.REMATCH_REQUESTED) : t("request_rematch") }}
                </button>
            </div>
            <router-link @click="resetConnection" to="/"
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

import {useWebSocketStore} from "@/stores/websocket";
import type {Ref} from 'vue'
import {onMounted, ref} from "vue";
import {ErrorCodes, WebSocketCodes} from "@/StatusCodes";
import router from "@/router";
import OpponentDisconnectedModal from "@/components/OpponentDisconnectedModal.vue";
import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";
import {getBaseURL} from "@/getBaseURL";
import {useI18n} from "vue-i18n";
import {onBeforeRouteLeave} from "vue-router";

const {t} = useI18n()

const OpponentDisconnectedModalRef = ref<InstanceType<typeof OpponentDisconnectedModal> | null>(null)

const currentStatusCode: Ref<null | string> = ref(null)

const isMyMove = ref(false)
const gameBoard: Ref<number[]> = ref([0, 0, 0, 0, 0, 0, 0, 0, 0])
const hasGameEnded = ref(false)

const rematchRequestAccept = ref(false)
const rematchBtnText = ref("Request Rematch")

let ws: WebSocket | null = null;

onMounted(() => {
    ws = useWebSocketStore().ws
    if (ws == null) {
        router.push({path: "/error", hash: `#${ErrorCodes.WEBSOCKET_IS_NULL}`})
    } else {
        startWebSocketListener()
    }
})

onBeforeRouteLeave((to, from) => {
    resetConnection()
})

function timeoutReached() {
    resetConnection()
}

function resetConnection() {
    ws?.close()
    useWebSocketStore().ws = null

    isMyMove.value = false
    hasGameEnded.value = false
    rematchRequestAccept.value = false
    rematchBtnText.value = t("request_rematch")
    gameBoard.value = [0, 0, 0, 0, 0, 0, 0, 0, 0]
}

function requestRematch() {
    console.log(WebSocketCodes.REQUEST_REMATCH.toString())
    if (!rematchRequestAccept.value) {
        ws?.send(WebSocketCodes.REQUEST_REMATCH.toString())
    } else {
        ws?.send(WebSocketCodes.ACCEPT_REMATCH.toString())
    }
}

function sendWebSocketData(number: number) {
    ws!.send(number.toString())
}

function startWebSocketListener() {
    ws!.onmessage = (event: any) => {
        const webSocketData = JSON.parse(event.data)
        console.log(webSocketData)

        if (webSocketData.statusCode == WebSocketCodes.YOUR_MOVE) {
            isMyMove.value = true
        } else if (webSocketData.statusCode == WebSocketCodes.OPPONENT_MOVE) {
            isMyMove.value = false
        }

        if (webSocketData.statusCode == WebSocketCodes.YOU_WON || webSocketData.statusCode == WebSocketCodes.OPPONENT_WON || webSocketData.statusCode == WebSocketCodes.GAME_ENDED_IN_DRAW) {
            hasGameEnded.value = true
        }

        if (webSocketData.statusCode == WebSocketCodes.OPPONENT_DISCONNECTED) {
            OpponentDisconnectedModalRef.value?.openModal()
        }

        if (webSocketData.statusCode == WebSocketCodes.FIRST_PLAYER_CONNECTED || webSocketData.statusCode == WebSocketCodes.SECOND_PLAYER_CONNECTED) {
            OpponentDisconnectedModalRef.value?.opponentReConnected()
        }

        if (webSocketData.statusCode != WebSocketCodes.GAME_BOARD) {
            currentStatusCode.value = webSocketData.statusCode
        }

        if (webSocketData.statusCode == WebSocketCodes.GAME_BOARD) {
            gameBoard.value = JSON.parse(webSocketData.msg.replace(/'/g, '"'))
        }

        if (webSocketData.statusCode == WebSocketCodes.REMATCH_REQUESTED) {
            rematchRequestAccept.value = true
            // rematchBtnText.value = t("ws_msg." + WebSocketCodes.REMATCH_REQUESTED)

        }
        if (webSocketData.statusCode == WebSocketCodes.REMATCH_ACCEPTED) {
            resetConnection()

            const gameCode = webSocketData.msg
            const protocol = location.protocol == 'https:' ? "wss" : "ws"
            useWebSocketStore().ws = new WebSocket(`${protocol}://${getBaseURL()}/ws?gameCode=${gameCode}`)
            ws = useWebSocketStore().ws
            router.push({path: "/play", query: {gameCode: gameCode}})
            startWebSocketListener()
        }
    }
}

</script>

<style scoped>
.field {
    @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>

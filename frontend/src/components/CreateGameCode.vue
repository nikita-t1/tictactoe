<template>
    <div>
        <div
            class="transition-all duration-700 flex flex-col w-96 h-80 bg-white border shadow-sm rounded-xl dark:bg-gray-800  dark:border-gray-700 dark:shadow-slate-700/[.7]">
            <div
                class="transition-all duration-700 bg-gray-100 border-b rounded-t-xl py-3 px-4 md:py-4 md:px-5 dark:bg-gray-800 dark:border-gray-700">
                <p class="transition-all duration-700 mt-1 text-sm text-gray-500 dark:text-gray-500">
                    {{ t("player") }} 1
                </p>
            </div>
            <div class="flex grow flex-col p-4 md:p-5">
                <h3 class="transition-all duration-700 flex-none text-lg font-bold text-gray-800 dark:text-white">
                    Card title
                </h3>
                <p class="transition-all duration-700 grow flex mt-2 text-gray-500 dark:text-gray-400 text-6xl font-mono h-fill items-center content-center justify-center" id="gameCode">
                    {{ gameCode }}
                </p>
                <a class="transition-all duration-700 flex-none mt-3 mx-auto w-full inline-flex items-center gap-2 text-sm font-medium text-blue-500 hover:text-blue-700">
                    <button type="button" @click="requestGameCode"
                            class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
                        {{ t("create_game_code") }}
                    </button>
                </a>
            </div>
        </div>

        <div :class="currentStatusCode != null ? 'visible opacity-100' : 'invisible opacity-0'"
             class="transition-all duration-1000">
            <div class="transition-all duration-700 flex mt-4 rounded-xl p-2 text-white">
                <div
                    class="animate-spin inline-block w-6 h-6 border-[3px] border-current border-t-transparent text-blue-600 rounded-full"
                    role="status" aria-label="loading">
                    <span class="sr-only">{{ t("loading") }}...</span>
                </div>
                <span
                    class="transition-all duration-700 ml-4 content-start justify-center text-gray-800 dark:text-white">
                    {{ t("ws_msg." + currentStatusCode) }} <!-- "ws_msg.4302" -->
                </span>

            </div>
        </div>

    </div>

</template>

<script setup lang="ts">
import axios from 'axios';
import {ref, watch} from "vue";
import {getBaseURLWithProtocol} from "@/getBaseURL";
import {useI18n} from "vue-i18n";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import {useWebSocketStore} from "@/stores/websocketStore";

const router = useRouter()

const gameCode = ref("----")

const {t} = useI18n()

const webSocketStore = useWebSocketStore()

const {currentStatusCode, bothPlayersConnected} = storeToRefs(webSocketStore)

function requestGameCode() {
    axios.defaults.baseURL = getBaseURLWithProtocol()
    axios.post("/create-game-code")
        .then((response) => {
            gameCode.value = response.data
            webSocketStore.init(gameCode.value)
        })
        .catch((error) => {
            //TODO: show error message to user
            console.error(error)
        })
}

watch(bothPlayersConnected, (newValue) => {
    if (newValue) {
        router.push({path: "/multiplayer/play", query: {gameCode: gameCode.value}})
    }
}, {
    immediate: true,
})

</script>

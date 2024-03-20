<template>
    <div class="">
        <div
            class="card bg-base-200 flex flex-col w-96 h-80">
            <div class="flex grow flex-col p-4 md:p-5 card-body">
                <h3 class=" flex-none text-lg font-bold">
                    {{ t("player") }} 1
                </h3>
                <p class=" grow flex mt-2 text-6xl font-mono h-fill items-center content-center justify-center" id="gameCode">
                    {{ gameCode }}
                </p>
                <a class=" flex-none mt-3 mx-auto w-full inline-flex items-center gap-2 text-sm font-medium">
                    <button type="button" @click="requestGameCode"
                            class="btn btn-primary w-full">
                        {{ t("create_game_code") }}
                    </button>
                </a>
            </div>
        </div>

        <div :class="currentStatusCode != null ? 'visible opacity-100' : 'invisible opacity-0'"
             class="">
            <div class=" flex mt-4 rounded-xl p-2">
                <span class="loading loading-spinner loading-md text-primary"/>
                <span class="ml-4 content-start justify-center">
                    {{ t(userMessage) }}
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

const {currentStatusCode, bothPlayersConnected, userMessage} = storeToRefs(webSocketStore)

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

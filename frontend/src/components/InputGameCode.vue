<template>
    <div>
        <div class="card bg-base-200 flex flex-col w-96 h-80">
            <div class="flex grow flex-col p-4 md:p-5 card-body">
                <h3 class="flex-none text-lg font-bold">
                    {{ t("player") }} 2
                </h3>
                <p class="grow mt-2 flex items-center content-center justify-center">
                    <input placeholder="----" ref="inputField" v-model="gameCode" type="text" autofocus
                           class="input text-center max-w-[10rem] min-h-[4rem] text-4xl font-mono py-4 px-4 block">
                </p>
                <a class="flex-none mt-3 inline-flex mx-auto w-full items-center gap-2 text-sm font-medium">
                    <button @click="startGame" type="button"
                            class="btn btn-primary w-full">
                        {{ t("start_playing") }}
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
import {type Ref, watch} from "vue";
import {onMounted, ref} from "vue";
import router from "@/router";
import {useWebSocketStore} from "@/stores/useMultiplayerWebsocketStore";
import {useI18n} from "vue-i18n";
import {storeToRefs} from "pinia";

const webSocketStore = useWebSocketStore()
const {bothPlayersConnected, userMessage} = storeToRefs(webSocketStore)

const {t} = useI18n()

const gameCode = ref("")
const currentStatusCode: Ref<null | string> = ref(null)

// Focus on the InputField
const inputField = ref<HTMLInputElement | null>(null)
onMounted(() => {
    inputField.value?.focus()
})


function startGame() {
    webSocketStore.init(gameCode.value)
}

watch(bothPlayersConnected, (newValue) => {
    if (newValue) {
        router.push({path: "/multiplayer/play", query: {gameCode: gameCode.value}})
    }
}, {
    immediate: true,
})

</script>

<style scoped>

</style>

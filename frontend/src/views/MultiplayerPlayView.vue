<template>
    <div class="transition-all duration-700 flex flex-col items-center justify-center mx-auto">
        <OpponentDisconnectedModal ref="OpponentDisconnectedModalRef"/>

        <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
            <div class="grid gap-x-4 gap-y-4 grid-cols-3">
                <div v-for="(item, index) in gameBoard" :key="index" class="field cursor-pointer"
                     :class="{'cursor-not-allowed' : !isMyMove}" @click="webSocketStore.sendWebSocketMsg(index+1)">
                    <IconCross v-if="item === 1"/>
                    <IconCircle v-if="item === 2"/>
                </div>
            </div>
        </div>

        <div class="transition-all duration-700 w-96 mt-4 p-2 text-center text-white">
        <span :class="statusCodeMessages != null ? 'visible opacity-100' : 'invisible opacity-0'"
              class="transition-all duration-700 text-gray-800 dark:text-white">
            {{ t("ws_msg." + statusCodeMessages) }} <!-- "ws_msg.4302" -->
         </span>
        </div>

        <div v-if="hasGameEnded"
             class="transition-all duration-700 flex flex-col md:flex-row max-w-xl content-center self-center items-center justify-center mx-auto space-x-2">
            <div
                class="flex-none mt-10 mx-auto inline-flex items-center gap-2 text-sm font-medium text-blue-500 hover:text-blue-700">
                <button type="button" @click="webSocketStore.requestRematch()"
                        class="transition-all duration-700 py-[.688rem] px-4 mx-auto w-48 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-600 hover:bg-blue-700 text-white text-sm dark:focus:ring-offset-gray-800">
                    {{ rematchRequestAccept ? t("ws_msg." + WebSocketCodes.REMATCH_REQUESTED) : t("request_rematch") }}
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
import {onMounted, ref} from "vue";
import {ErrorCodes, WebSocketCodes} from "@/StatusCodes";
import router from "@/router";
import OpponentDisconnectedModal from "@/components/OpponentDisconnectedModal.vue";
import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";
import {useI18n} from "vue-i18n";
import {onBeforeRouteLeave} from "vue-router";
import {storeToRefs} from "pinia";

const {t} = useI18n()

const webSocketStore = useWebSocketStore()
const {ws, gameBoard, hasGameEnded, statusCodeMessages, isMyMove, rematchRequestAccept} = storeToRefs(webSocketStore)

const OpponentDisconnectedModalRef = ref<InstanceType<typeof OpponentDisconnectedModal> | null>(null)

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

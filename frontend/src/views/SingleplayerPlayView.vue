<template>
    <div class="transition-all duration-700 flex flex-col items-center justify-center mx-auto">

        <!-- Game board -->
        <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
            <div class="grid gap-x-4 gap-y-4 grid-cols-3">
                <div v-for="(item, index) in gameBoard" :key="index" class="field cursor-pointer"
                     :class="{'cursor-not-allowed' : awaitingMoveBy != MOVE_BY_PLAYER}"
                     @click="gameStore.playerMove(index)">
                    <IconCross v-if="item === MOVE_BY_PLAYER"/>
                    <IconCircle v-if="item === MOVE_BY_COMPUTER"/>
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
                <button type="button" @click="gameStore.replay"
                        class="transition-all duration-700 py-[.688rem] px-4 mx-auto w-48 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-600 hover:bg-blue-700 text-white text-sm dark:focus:ring-offset-gray-800">
                    {{ t("replay") }}
                </button>
            </div>
            <router-link @click="gameStore.replay" to="/"
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

import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";
import {useI18n} from "vue-i18n";
import {useGameStore} from "@/stores/useGameStore";
import {storeToRefs} from "pinia";
import {MOVE_BY_COMPUTER, MOVE_BY_PLAYER} from "@/helper/GameBoardHelper";
import {onMounted} from "vue";

const {t} = useI18n()

const gameStore = useGameStore()
const {currentStatusCode, hasGameEnded, awaitingMoveBy, gameBoard} = storeToRefs(gameStore)
onMounted(() => gameStore.replay())

</script>

<style scoped>
.field {
    @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>

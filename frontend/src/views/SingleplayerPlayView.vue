<template>
    <div class="flex flex-col items-center justify-center mx-auto">

        <GameBoard :gameBoard="gameBoard" :awaiting-move-by="awaitingMoveBy"
                   @player-move="(index) => gameStore.playerMove(index)"/>

        <!-- Status message -->
        <div class=" w-96 mt-4 p-2 text-center bg-base-100">
            {{ t(userMessage) }}
        </div>

        <!-- Replay and Home buttons -->
        <div v-if="hasGameEnded"
             class="flex flex-col md:flex-row max-w-xl content-center self-center items-center justify-center mx-auto space-x-2">
            <div
                class="flex-none mt-10 mx-auto inline-flex items-center gap-2 text-sm font-medium">
                <button type="button" @click="gameStore.replay" id="replay_btn"
                        class="btn btn-primary py-[.688rem] px-4 mx-auto w-48 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold text-sm">
                    {{ t("replay") }}
                </button>
            </div>
            <router-link @click="gameStore.replay" to="/"
                         class="flex-none mx-auto inline-flex items-center gap-2 mt-10  text-sm font-medium">
                <button type="button"
                        class="btn btn-ghost py-[.688rem] px-4 w-48 inline-flex justify-center items-center gap-2 rounded-md border-2 border-gray-200 font-semibold text-sm">
                    {{ t("home") }}
                </button>
            </router-link>

        </div>
    </div>


</template>

<script setup lang="ts">

import GameBoard from "@/components/GameBoard.vue";

import {useI18n} from "vue-i18n";
import {useSingleplayerGameStore} from "@/stores/useSingleplayerGameStore";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";

const {t} = useI18n()

const gameStore = useSingleplayerGameStore()
const {userMessage, hasGameEnded, awaitingMoveBy, gameBoard} = storeToRefs(gameStore)
onMounted(() => gameStore.replay())

</script>


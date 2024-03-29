<script setup lang="ts">
import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";

import {MOVE_BY_OPPONENT, MOVE_BY_PLAYER} from "@/helper/GameBoardHelper";
import type {PropType} from "vue";

const props = defineProps({
    gameBoard: {
        type: Array as PropType<number[]>,
        required: true
    },
    awaitingMoveBy: Number
})

const emit = defineEmits<{
    (e: 'playerMove', index: number): void
}>()

function playerMove(index: number) {
    if (props.awaitingMoveBy != MOVE_BY_PLAYER) return
    emit('playerMove', index)
}
</script>

<template>
    <div class="">
        <div class="grid gap-x-4 gap-y-4 grid-cols-3">
            <div v-for="(item, index) in gameBoard" :key="index" class="field cursor-pointer rounded-box"
                 :class="{'cursor-not-allowed' : awaitingMoveBy != MOVE_BY_PLAYER}"
                 @click="playerMove( index)">
                <IconCross class="text-primary scale-[0.8] md:scale-100" v-if="item === MOVE_BY_PLAYER"/>
                <IconCircle class="text-primary scale-[0.8] md:scale-100" v-if="item === MOVE_BY_OPPONENT"/>
            </div>
        </div>
    </div>
</template>

<style scoped>
.field {
    @apply bg-base-200 border border-primary aspect-square w-24 h-24 sm:w-28 sm:h-28 md:w-32 md:h-32 flex justify-center content-center items-center text-6xl
}

</style>

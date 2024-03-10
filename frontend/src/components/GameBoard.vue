<script setup lang="ts">
import IconCircle from "@/components/icons/IconCircle.vue";
import IconCross from "@/components/icons/IconCross.vue";

import {MOVE_BY_COMPUTER, MOVE_BY_PLAYER} from "@/helper/GameBoardHelper";

const props = defineProps({
    gameBoard: Array,
    awaitingMoveBy: Number
})

const emit = defineEmits<{
    (e: 'playerMove', index: number): void
}>()
</script>

<template>
    <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
        <div class="grid gap-x-4 gap-y-4 grid-cols-3">
            <div v-for="(item, index) in gameBoard" :key="index" class="field cursor-pointer"
                 :class="{'cursor-not-allowed' : awaitingMoveBy != MOVE_BY_PLAYER}"
                 @click="emit('playerMove', index)">
                <IconCross v-if="item === MOVE_BY_PLAYER"/>
                <IconCircle v-if="item === MOVE_BY_COMPUTER"/>
            </div>
        </div>
    </div>
</template>

<style scoped>
.field {
    @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>

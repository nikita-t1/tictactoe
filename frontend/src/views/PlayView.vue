<template>
  <div class="transition-all duration-700 flex flex-col h-screen items-center justify-center mx-auto">

    <div class="transition-all duration-700 max-w-4xl space-x-8 space-y-4">
      <div class="grid gap-x-4 gap-y-4 grid-cols-3">

        <div v-for="(item, index) in gameBoard" class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(index+1)">{{ item }}</div>

      </div>
    </div>

    <div class="transition-all duration-700 w-96 mt-4 p-2 text-center text-white">
        <span class="transition-all duration-700 text-gray-800 dark:text-white">
        {{ msg }}
      </span>
    </div>
  </div>


</template>

<script setup lang="ts">

import {useWebSocketStore} from "@/stores/websocket";
import {onMounted, ref} from "vue";
import type {Ref} from 'vue'
import {ErrorCodes, MessageMap, WebSocketCodes} from "@/StatusCodes";
import router from "@/router";

const msg = ref("What have you expected to see?")
const isMyMove = ref(false)
const gameBoard: Ref<number[]> = ref([0, 0, 0, 0, 0, 0, 0, 0, 0])
const hasGameWinner = ref(false)

let ws: WebSocket | null = null;

onMounted(() => {
  ws = useWebSocketStore().ws
    if (ws == null) {
      router.push({path: "/error", hash: `#${ErrorCodes.WEBSOCKET_IS_NULL}`})
    } else {
      startWebSocketListener()
    }
})

function sendWebSocketData(number: number) {
  ws!.send(number.toString())
}

function startWebSocketListener() {
  ws!.onmessage = (event: any) => {
    const webSocketData = JSON.parse(event.data)

    if (webSocketData.statusCode == WebSocketCodes.YOUR_MOVE){
      isMyMove.value = true
    } else if (webSocketData.statusCode == WebSocketCodes.OPPONENT_MOVE) {
      isMyMove.value = false
    }

    if (webSocketData.statusCode == WebSocketCodes.YOU_WON || webSocketData.statusCode == WebSocketCodes.OPPONENT_WON){
      hasGameWinner.value = true
    }

    if (webSocketData.statusCode != WebSocketCodes.GAME_BOARD){
      msg.value = MessageMap.get(parseInt(webSocketData.statusCode))
    }

    if (webSocketData.statusCode == WebSocketCodes.GAME_BOARD) {
      gameBoard.value = JSON.parse(webSocketData.msg.replace(/'/g, '"'))
    }
  }
}

</script>

<style scoped>
.field {
  @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>
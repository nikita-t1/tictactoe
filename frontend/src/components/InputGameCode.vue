<template>
  <div class="flex flex-col w-96 h-80 bg-white border shadow-sm rounded-xl dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
    <div class="bg-gray-100 border-b rounded-t-xl py-3 px-4 md:py-4 md:px-5 dark:bg-gray-800 dark:border-gray-700">
      <p class="mt-1 text-sm text-gray-500 dark:text-gray-500">
        Player 2
      </p>
    </div>
    <div class="flex grow flex-col p-4 md:p-5">
      <h3 class="flex-none text-lg font-bold text-gray-800 dark:text-white">
        Card title
      </h3>
      <p class="grow mt-2 flex text-gray-800 dark:text-gray-400 items-center content-center justify-center">
        <input v-model="gameCode" type="text" class="max-w-[10rem] text-2xl font-mono py-3 px-4 block border-gray-200 rounded-md text-sm focus:border-blue-500 focus:ring-blue-500 dark:bg-slate-900 dark:border-gray-700 dark:text-gray-400">
      </p>
      <a class="flex-none mt-3 inline-flex mx-auto w-full items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
        <button @click="startGame" type="button" class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border-2 border-gray-200 font-semibold text-blue-500 hover:text-white hover:bg-blue-500 hover:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:border-gray-700 dark:hover:border-blue-500">
          Start Playing
        </button>
      </a>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import router from "@/router";
import {useWebSocketStore} from "@/stores/websocket";
import webSocketDataCode from "@/WebSocketDataCode";

const gameCode = ref("")

function startGame(){

  const ws = new WebSocket("ws://127.0.0.1:8080/ws?gameCode=" + gameCode.value);
  useWebSocketStore().ws = ws

  ws.onmessage = (event) => {
    console.log(event.data)
    const webSocketData = JSON.parse(event.data)
    if (webSocketData.statusCode == webSocketDataCode.STATUS_OK) {
      router.push({path: "/play", query: {gameCode: gameCode.value}})
    } else {
      // TODO: Handle this
    }
  }
}

</script>

<style scoped>

</style>
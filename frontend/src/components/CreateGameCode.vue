<template>
  <div
      class="flex flex-col w-2/5 h-80 bg-white border shadow-sm rounded-xl dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
    <div class="bg-gray-100 border-b rounded-t-xl py-3 px-4 md:py-4 md:px-5 dark:bg-gray-800 dark:border-gray-700">
      <p class="mt-1 text-sm text-gray-500 dark:text-gray-500">
        Player 1
      </p>
    </div>
    <div class="flex grow flex-col p-4 md:p-5">
      <h3 class="flex-none text-lg font-bold text-gray-800 dark:text-white">
        Card title
      </h3>
      <p class="grow flex mt-2 text-gray-800 dark:text-gray-400 text-6xl font-mono h-fill items-center content-center justify-center">
        {{ gameCode }}
      </p>
      <a class="flex-none  mt-3 mx-auto w-full inline-flex items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
        <button type="button" @click="requestGameCode"
                class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border-2 border-gray-200 font-semibold text-blue-500 hover:text-white hover:bg-blue-500 hover:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:border-gray-700 dark:hover:border-blue-500">
          Create Game Code
        </button>
      </a>
    </div>
  </div>
</template>

<script setup lang="ts">
import axios, {isCancel, AxiosError} from 'axios';
import {ref} from "vue";
import router from "@/router";
import {useWebSocketStore} from "@/stores/websocket";
import WebSocketDataCode from "@/WebSocketDataCode";

const gameCode = ref("----")

function requestGameCode() {
  axios.post("http://127.0.0.1:8080/create-game-code")
      .then((response) => {
        gameCode.value = response.data

        const ws = new WebSocket("ws://127.0.0.1:8080/ws?gameCode=" + gameCode.value);
        useWebSocketStore().ws = ws

        ws.onmessage = (event) => {
          console.log(event.data)
          const webSocketData = JSON.parse(event.data)
          if (webSocketData.statusCode == WebSocketDataCode.GAME_CODE_TIMEOUT_REACHED){
            //TODO: Handle Timeout
          }
          if (webSocketData.statusCode == WebSocketDataCode.SECOND_PLAYER_CONNECTED) {
            router.push({path: "/play", query: {gameCode: gameCode.value}})
          }
        }
      })
}

</script>

<style scoped>

</style>
<template>
  <div>
    <div class="transition-all duration-700 flex flex-col w-96 h-80 bg-white border shadow-sm rounded-xl dark:bg-gray-800 dark:border-gray-700 dark:shadow-slate-700/[.7]">
      <div class="transition-all duration-700 bg-gray-100 border-b rounded-t-xl py-3 px-4 md:py-4 md:px-5 dark:bg-gray-800 dark:border-gray-700">
        <p class="transition-all duration-700 mt-1 text-sm text-gray-500 dark:text-gray-500">
          Player 2
        </p>
      </div>
      <div class="flex grow flex-col p-4 md:p-5">
        <h3 class="transition-all duration-700 flex-none text-lg font-bold text-gray-800 dark:text-white">
          Card title
        </h3>
        <p class="transition-all duration-700 grow mt-2 flex text-gray-800 dark:text-gray-400 items-center content-center justify-center">
          <input ref="inputField" v-model="gameCode" type="text" autofocus
                 class="transition-all duration-700 max-w-[10rem] text-2xl font-mono bg-gray-100 py-3 px-4 block border border-gray-200 rounded-md text-sm focus:border-blue-500 focus:ring-blue-500 dark:bg-slate-900 dark:border-gray-700 dark:text-gray-400">
        </p>
        <a class="flex-none mt-3 inline-flex mx-auto w-full items-center gap-2 mt-5 text-sm font-medium text-blue-500 hover:text-blue-700">
          <button @click="startGame" type="button"
                  class="py-[.688rem] px-4 mx-auto w-full inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800">
            Start Playing
          </button>
        </a>
      </div>
    </div>

    <div :class="msg !== '' ? 'visible opacity-100' : 'invisible opacity-0'" class="transition-all duration-1000">
      <div
          class="transition-all duration-700 flex mt-4 rounded-xl p-2 text-white bg-white border shadow-sm rounded-xl dark:bg-gray-800  dark:border-gray-700 dark:shadow-slate-700/[.7]">
        <div
            class="animate-spin inline-block w-6 h-6 border-[3px] border-current border-t-transparent text-blue-600 rounded-full"
            role="status" aria-label="loading">
          <span class="sr-only">Loading...</span>
        </div>
        <span class="transition-all duration-700 ml-4 content-start justify-center text-gray-800 dark:text-white">
        {{ msg }}
      </span>

      </div>
    </div>

  </div>

</template>

<script setup lang="ts">
import {onMounted, onRenderTracked, onRenderTriggered, ref} from "vue";
import router from "@/router";
import {useWebSocketStore} from "@/stores/websocket";
import {getBaseURL} from "@/getBaseURL";
import {MessageMap, WebSocketCodes} from "@/StatusCodes";

const gameCode = ref("")
const msg = ref("")

// Focus on the InputField
const inputField = ref<HTMLInputElement | null>(null)
onMounted(() => {
  inputField.value?.focus()
})


function startGame() {

  const protocol = location.protocol == 'https:' ? "wss" : "ws"
  const ws = new WebSocket(`${protocol}://${getBaseURL()}/ws?gameCode=${gameCode.value}`);
  useWebSocketStore().ws = ws

  ws.onmessage = (event) => {
    console.log(event.data)
    const webSocketData = JSON.parse(event.data)
    msg.value = MessageMap.get(parseInt(webSocketData.statusCode))

    if (webSocketData.statusCode == WebSocketCodes.NO_SECOND_PLAYER_YET) {
      // Server doesn't distinguish between first/second Player,
      // but this Vue Component always belongs to the Second user and the second user doesn't have to wait for someone
      msg.value = MessageMap.get(WebSocketCodes.NO_GAME_WITH_THIS_CODE_FOUND)
      useWebSocketStore().ws?.close()
    }

    if (webSocketData.statusCode == WebSocketCodes.STATUS_OK) {
      router.push({path: "/play", query: {gameCode: gameCode.value}})
    }

  }
}

</script>

<style scoped>

</style>
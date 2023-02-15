<template>
  <div class="flex flex-col h-screen items-center justify-center mx-auto">
    <div class=" max-w-4xl  space-x-8 space-y-4">
      <div class="grid gap-x-4 gap-y-4 grid-cols-3">
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(1)">{{one}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(2)">{{two}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(3)">{{three}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(4)">{{four}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(5)">{{five}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(6)">{{six}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(7)">{{seven}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(8)">{{eight}}</div>
        <div class="field cursor-pointer" :class="{'cursor-not-allowed' : !isMyMove}" @click="sendWebSocketData(9)">{{nine}}</div>
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
import {ErrorCodes, MessageMap, WebSocketCodes} from "@/StatusCodes";
import router from "@/router";

const one = ref("")
const two = ref("")
const three = ref("")
const four = ref("")
const five = ref("")
const six = ref("")
const seven = ref("")
const eight = ref("")
const nine = ref("")

const msg = ref("What have you expected to see?")
const isMyMove = ref(false)

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
    // alert(webSocketData.statusCode)
    switch (parseInt(webSocketData.statusCode)) {

      case WebSocketCodes.STATUS_OK:
        msg.value = "Status OK"
        break
      case WebSocketCodes.YOUR_MOVE:
        msg.value = MessageMap.get(parseInt(webSocketData.statusCode))
        isMyMove.value = true
        break
      case WebSocketCodes.OPPONENT_MOVE:
        msg.value = "It's your Opponent's Move"
        isMyMove.value = false
        break
      case WebSocketCodes.MOVE_INVALID:
        msg.value = "Move is not valid"
        break
      case WebSocketCodes.NOT_YOUR_TURN:
        msg.value = "It's not your turn"
        break
      case WebSocketCodes.YOU_WON:
        msg.value = "You Won"
        break
      case WebSocketCodes.OPPONENT_WON:
        msg.value = "Opponent Won"
        break

    }
    msg.value = MessageMap.get(parseInt(webSocketData.statusCode))

    if (webSocketData.statusCode == WebSocketCodes.GAME_BOARD) {
      const gameBoard = JSON.parse(webSocketData.msg)

      console.log(gameBoard.one)
      console.log(gameBoard.two)
      console.log(gameBoard.three)
      console.log(gameBoard.four)
      console.log(gameBoard.five)
      console.log(gameBoard.six)
      console.log(gameBoard.seven)
      console.log(gameBoard.eight)
      console.log(gameBoard.nine)

      one.value =  String(gameBoard.one)
      two.value =  String(gameBoard.two)
      three.value =  String(gameBoard.three)
      four.value =  String(gameBoard.four)
      five.value =  String(gameBoard.five)
      six.value =  String(gameBoard.six)
      seven.value =  String(gameBoard.seven)
      eight.value =  String(gameBoard.eight)
      nine.value =  String(gameBoard.nine)
      // for (const prop in JSON.parse(webSocketData.msg)) {
      //   console.log(prop)
      // }
    }
  }
}

</script>

<style scoped>
.field {
  @apply transition-all duration-700 bg-white dark:bg-gray-800 text-gray-500 dark:text-gray-500 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl outline outline-2 outline-offset-1 outline-blue-500 rounded-lg
}

</style>
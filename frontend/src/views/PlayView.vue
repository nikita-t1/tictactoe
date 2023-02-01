<template>
  <div class="flex max-w-4xl content-center self-center items-center justify-center mx-auto space-x-8 h-screen">
    <div class="grid gap-x-4 gap-y-4 grid-cols-3">
      <div class="field" @click="sendWebSocketData(1)">{{one}}</div>
      <div class="field" @click="sendWebSocketData(2)">{{two}}</div>
      <div class="field" @click="sendWebSocketData(3)">{{three}}</div>
      <div class="field" @click="sendWebSocketData(4)">{{four}}</div>
      <div class="field" @click="sendWebSocketData(5)">{{five}}</div>
      <div class="field" @click="sendWebSocketData(6)">{{six}}</div>
      <div class="field" @click="sendWebSocketData(7)">{{seven}}</div>
      <div class="field" @click="sendWebSocketData(8)">{{eight}}</div>
      <div class="field" @click="sendWebSocketData(9)">{{nine}}</div>
    </div>
  </div>
</template>

<script setup lang="ts">


import {useWebSocketStore} from "@/stores/websocket";
import {onMounted, ref} from "vue";
import WebSocketDataCode from "@/WebSocketDataCode";

const one = ref("")
const two = ref("")
const three = ref("")
const four = ref("")
const five = ref("")
const six = ref("")
const seven = ref("")
const eight = ref("")
const nine = ref("")

onMounted(() => {
  startWebSocketListener()
})

function sendWebSocketData(number: number){
  useWebSocketStore().ws.send(number)
}

function startWebSocketListener(){
  useWebSocketStore().ws.onmessage = (event: any) => {
    const webSocketData = JSON.parse(event.data)
    if (webSocketData.statusCode == WebSocketDataCode.GAME_BOARD){
      const gameBoard =JSON.parse(webSocketData.msg)

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
  @apply bg-red-200 aspect-square w-32 font-mono flex h-full justify-center content-center items-center text-6xl cursor-pointer
}

</style>
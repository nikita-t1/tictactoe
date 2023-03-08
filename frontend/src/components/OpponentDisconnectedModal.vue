<template>
  <div ref="opponentDisconnectedModal" id="hs-opponentDisconnectedModal" class="[--overlay-backdrop:static] hs-overlay hidden w-full h-full fixed top-0 left-0 z-[60] overflow-x-hidden overflow-y-auto">

    <div class=" hs-overlay-open:mt-7 hs-overlay-open:opacity-100 hs-overlay-open:duration-500 mt-0 opacity-0 ease-out transition-all sm:max-w-lg sm:w-full m-3 sm:mx-auto">
      <div class="relative flex flex-col bg-white shadow-lg rounded-xl dark:bg-gray-800">

        <div class="p-4 sm:p-10 text-center overflow-y-auto">
          <!-- Icon -->
          <span class="mb-4 inline-flex justify-center items-center w-[62px] h-[62px] rounded-full border-4 border-yellow-50 bg-yellow-100 text-yellow-500 dark:bg-yellow-700 dark:border-yellow-600 dark:text-yellow-100">
          <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
            <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
          </svg>
        </span>
          <!-- End Icon -->

          <h3 class="mb-2 text-2xl font-bold text-gray-800 dark:text-gray-200">
              {{ $t("opponent_disconnected")}}
          </h3>
          <p class="transition-all duration-700 text-gray-800 dark:text-white">
              {{ $t("opponent_disconnected_desc")}}
          </p>

          <div class="mt-6 flex justify-center gap-x-4">
            <a v-if="!countDownRanOut" class="py-2.5 px-4 inline-flex justify-center items-center gap-2 rounded-md border font-medium bg-white text-gray-700 shadow-sm align-middle hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-white focus:ring-blue-600 transition-all text-sm dark:bg-gray-800 dark:hover:bg-slate-800 dark:border-gray-700 dark:text-gray-400 dark:hover:text-white dark:focus:ring-offset-gray-800" href="#">
                {{ $t("wait_for_opponent_reconnect")}}... ({{countDown}})
            </a>
            <router-link to="/" @click="reset">
              <button type="button" class="py-2.5 px-4 inline-flex justify-center items-center gap-2 rounded-md border border-transparent font-semibold bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all text-sm dark:focus:ring-offset-gray-800" data-hs-overlay="#hs-sign-out-alert">
                  {{ $t("close_game")}}
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup lang="ts">
import {onBeforeUnmount, ref} from "vue";
const emit = defineEmits(['timeoutReached'])

let opponentDisconnectedModal = ref<HTMLDivElement | null>(null)

const countDown = ref(60)

const countDownIsRunning = ref(false)
const countDownRanOut = ref(false)
const isOpponentConnected = ref(false)

onBeforeUnmount(() => {
  reset()
})

function countDownDecrement(){
  console.log(countDown.value)
  if (countDownIsRunning.value && !isOpponentConnected.value){
    if (countDown.value >= 1){
      countDown.value = countDown.value-1
      setTimeout(countDownDecrement, 1000);
    } else timeoutReached()
  }
}

function timeoutReached(){
  emit('timeoutReached')
  countDownRanOut.value = true
}

function reset(){
  countDown.value = 60;
  countDownIsRunning.value = false
  countDownRanOut.value = false
  isOpponentConnected.value = false
  closeModal()
}

function opponentReConnected(){
  isOpponentConnected.value = true
  reset()
}

//It all starts here...
function openModal() {
  reset();
  countDownIsRunning.value = true;
  (window as any).HSOverlay!.open(opponentDisconnectedModal.value)
  countDownDecrement()
}

function closeModal(){
  // Preline doesn't have TypeScript Support. See: https://github.com/htmlstreamofficial/preline/issues/28
  (window as any).HSOverlay!.close(opponentDisconnectedModal.value)
}

defineExpose({openModal, opponentReConnected})

</script>

<style scoped>

</style>

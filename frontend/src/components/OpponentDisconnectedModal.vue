<template>
    <div>
        <dialog ref="opponentDisconnectedModal" id="opponentDisconnectedModal"
                class="modal modal-bottom sm:modal-middle">
            <div class="modal-box">
                <div class="w-full flex flex-col justify-center items-center">

                    <!-- Icon -->
                    <span
                        class="mb-4 inline-flex justify-center items-center w-[62px] h-[62px] rounded-full border-4 bg-warning text-warning-content">
                            <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                                 viewBox="0 0 16 16">
                                <path
                                    d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                            </svg>
                    </span>
                    <h3 class="mb-2 text-2xl font-bold">
                        {{ $t("opponent_disconnected") }}
                    </h3>
                    <p class="">
                        {{ $t("opponent_disconnected_desc") }}
                    </p>
                </div>

                <!-- Buttons -->
                <div class="modal-action w-full flex justify-between ">
                    <a class="btn"
                       href="#">
                        {{ t("wait_for_opponent_reconnect") }}... ({{ countDown }})
                    </a>
                    <router-link to="/" @click="reset">
                        <button type="button"
                                class="btn btn-primary">
                            {{ t("close_game") }}
                        </button>
                    </router-link>
                </div>
            </div>
        </dialog>
    </div>

</template>

<script setup lang="ts">
import {onBeforeUnmount, ref, watch} from "vue";
import {useI18n} from "vue-i18n";
import {useWebSocketStore} from "@/stores/useMultiplayerWebsocketStore";
import {storeToRefs} from "pinia";

const webSocketStore = useWebSocketStore()
const {bothPlayersConnected, gameBoard} = storeToRefs(webSocketStore)

const {t} = useI18n()

const opponentDisconnectedModal = ref<HTMLDialogElement | null>(null)

const countDown = ref(60)

const countDownIsRunning = ref(false)
const countDownRanOut = ref(false)
const isOpponentConnected = ref(false)

watch(bothPlayersConnected, (value) => {
    if (value == false) {
        openModal()
    } else {
        reset()
    }
})

onBeforeUnmount(() => {
    reset()
})

function countDownDecrement() {
    console.log(countDown.value)
    if (countDownIsRunning.value && !isOpponentConnected.value) {
        if (countDown.value >= 1) {
            countDown.value = countDown.value - 1
            setTimeout(countDownDecrement, 1000);
        } else timeoutReached()
    }
}

function timeoutReached() {
    webSocketStore.reset()
    countDownRanOut.value = true
}

function reset() {
    countDown.value = 60;
    countDownIsRunning.value = false
    countDownRanOut.value = false
    isOpponentConnected.value = false
    closeModal()
}

function openModal() {
    reset();
    opponentDisconnectedModal.value?.showModal()
    countDownIsRunning.value = true;
    countDownDecrement()
}

function closeModal() {
    opponentDisconnectedModal.value?.close()
}


</script>

<style scoped>

</style>

import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useWebSocketStore = defineStore('websocket', () => {
    const ws = ref()

    return { ws }
})

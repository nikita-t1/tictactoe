import {ref} from 'vue'
import type {Ref} from 'vue'
import { defineStore } from 'pinia'

export const useWebSocketStore = defineStore('websocket', (): { ws: Ref<WebSocket | null> } => {
    const ws = ref<WebSocket | null>(null)

    return {ws}
})

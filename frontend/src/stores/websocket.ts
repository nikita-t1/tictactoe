import {computed, type Ref, ref} from 'vue'
import {defineStore} from 'pinia'
import {getBaseURL} from "@/getBaseURL";
import {WebSocketCodes} from "@/StatusCodes";
import {useEventBus, useRefHistory} from "@vueuse/core";
import router from "@/router";

export const useWebSocketStore = defineStore('websocket', () => {
    const ws = ref<WebSocket | null>(null)

    const gameCode: Ref<null | string> = ref(null)
    const currentStatusCode: Ref<string> = ref("")
    const { history } = useRefHistory(currentStatusCode)
    const statusCodeMessages = computed(() => {
        return history.value.filter((item) => item.snapshot != WebSocketCodes.GAME_BOARD.toString())[0].snapshot
    })

    const isMyMove = ref(false)
    const gameBoard: Ref<number[]> = ref([0, 0, 0, 0, 0, 0, 0, 0, 0])
    const hasGameEnded = ref(false)

    const rematchRequestAccept = ref(false)
    const opponentDisconnectedBus = useEventBus<boolean>('opponentDisconnected')

    let bothPlayersConnectedCallback: () => void = () => {};


    const init = (newGameCode: string) => {
        gameCode.value = newGameCode
        const protocol = location.protocol == 'https:' ? "wss" : "ws"
        ws.value = new WebSocket(`${protocol}://${getBaseURL()}/ws?gameCode=${gameCode.value}`);
        ws.value.onmessage = onMessageEvent
    }

    const sendWebSocketMsg = (msg: string | number) => {
        if (ws.value == null) return
        ws.value.send(msg.toString())
    }

    const onMessageEvent = (event: MessageEvent) => {
        console.log(event.data)
        const webSocketData = JSON.parse(event.data)
        currentStatusCode.value = webSocketData.statusCode

        if (webSocketData.statusCode == WebSocketCodes.SECOND_PLAYER_CONNECTED || webSocketData.statusCode == WebSocketCodes.FIRST_PLAYER_CONNECTED) {
            bothPlayersConnectedCallback()
        }

        if (webSocketData.statusCode == WebSocketCodes.YOUR_MOVE) {
            isMyMove.value = true
        } else if (webSocketData.statusCode == WebSocketCodes.OPPONENT_MOVE) {
            isMyMove.value = false
        }

        if (webSocketData.statusCode == WebSocketCodes.YOU_WON || webSocketData.statusCode == WebSocketCodes.OPPONENT_WON || webSocketData.statusCode == WebSocketCodes.GAME_ENDED_IN_DRAW) {
            hasGameEnded.value = true
        }

        if (webSocketData.statusCode == WebSocketCodes.OPPONENT_DISCONNECTED) {
            opponentDisconnectedBus.emit(true)
        }

        if (webSocketData.statusCode == WebSocketCodes.FIRST_PLAYER_CONNECTED || webSocketData.statusCode == WebSocketCodes.SECOND_PLAYER_CONNECTED) {
            opponentDisconnectedBus.emit(false)
        }

        if (webSocketData.statusCode != WebSocketCodes.GAME_BOARD) {
            currentStatusCode.value = webSocketData.statusCode
        }

        if (webSocketData.statusCode == WebSocketCodes.GAME_BOARD) {
            gameBoard.value = JSON.parse(webSocketData.msg.replace(/'/g, '"'))
        }

        if (webSocketData.statusCode == WebSocketCodes.REMATCH_REQUESTED) {
            rematchRequestAccept.value = true
            // rematchBtnText.value = t("ws_msg." + WebSocketCodes.REMATCH_REQUESTED)
        }
        if (webSocketData.statusCode == WebSocketCodes.REMATCH_ACCEPTED) {
            reset()

            const gameCode = webSocketData.msg
            init(gameCode)
            router.push({path: "/multiplayer/play", query: {gameCode: gameCode}})
        }
    }

    const requestRematch = () => {
        if (ws.value == null) return
        console.log(WebSocketCodes.REQUEST_REMATCH.toString())
        if (!rematchRequestAccept.value) {
            ws.value.send(WebSocketCodes.REQUEST_REMATCH.toString())
        } else {
            ws.value.send(WebSocketCodes.ACCEPT_REMATCH.toString())
        }
    }

    const setBothPlayersConnectedCallback = (callback: () => void) => {
        bothPlayersConnectedCallback = callback
    }

    function reset() {
        gameCode.value = null
        currentStatusCode.value = ""
        isMyMove.value = false
        gameBoard.value = [0, 0, 0, 0, 0, 0, 0, 0, 0]
        hasGameEnded.value = false
        rematchRequestAccept.value = false
        ws.value?.close()
    }

    return {
        init,
        reset,
        sendWebSocketMsg,
        setBothPlayersConnectedCallback,
        ws,
        gameCode,
        statusCodeMessages,
        isMyMove,
        gameBoard,
        hasGameEnded,
        rematchRequestAccept,
        requestRematch,

    }
})

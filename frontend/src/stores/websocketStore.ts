import {type Ref, ref} from 'vue'
import {defineStore} from 'pinia'
import {getBaseURL} from "@/getBaseURL";
import {WebSocketCodes} from "@/StatusCodes";
import router from "@/router";
import type WebSocketRequest from "@/types/WebSocketRequest";
import type WebSocketResponse from "@/types/WebSocketResponse";

export const useWebSocketStore = defineStore('websocket', () => {
    const ws = ref<WebSocket | null>(null)

    const gameCode: Ref<null | string> = ref(null)
    const currentStatusCode: Ref<number> = ref(WebSocketCodes.STATUS_OK)

    const bothPlayersConnected = ref(false)
    const playerNumber = ref(0)
    const isMyMove = ref(false)
    const gameBoard: Ref<number[]> = ref([0, 0, 0, 0, 0, 0, 0, 0, 0])
    const hasGameEnded = ref(false)

    const rematchRequested = ref(false)
    // const opponentDisconnectedBus = useEventBus<boolean>('opponentDisconnected')

    const init = (newGameCode: string) => {
        gameCode.value = newGameCode
        const protocol = location.protocol == 'https:' ? "wss" : "ws"
        ws.value = new WebSocket(`${protocol}://${getBaseURL()}/ws?gameCode=${gameCode.value}`);
        ws.value.onmessage = onMessageEvent
    }

    const sendWebSocketMsg = (msg: string) => {
        if (ws.value == null) return
        ws.value.send(msg.toString())
    }

    const sendGameBoardMove = (index: number) => {
        const req: WebSocketRequest = {
            gameCode: gameCode.value ?? "",
            playerNumber: playerNumber.value,
            playerMoveAtIndex: index,
            rematchRequestedByPlayer: 0 // No rematch requested
        }
        sendWebSocketMsg(JSON.stringify(req))
    }

    const onMessageEvent = (event: MessageEvent) => {
        const webSocketData: WebSocketResponse = JSON.parse(event.data)
        console.log(webSocketData)
        // if (webSocketData.gameCode != gameCode.value) throw new Error("Game code mismatch")
        currentStatusCode.value = webSocketData.statusCode

        if (webSocketData.playerNumber > 0) {
            playerNumber.value = webSocketData.playerNumber;
        }
        isMyMove.value = webSocketData.awaitMoveFromPlayer == playerNumber.value
        console.log("Player number: ", playerNumber.value, "Awaiting move by: ", webSocketData.awaitMoveFromPlayer)
        console.log("Is my move: ", isMyMove.value)
        gameBoard.value = webSocketData.gameBoard
        hasGameEnded.value = webSocketData.hasGameEnded
        console.log("Both players connected: ", webSocketData.bothPlayersConnected)
        bothPlayersConnected.value = webSocketData.bothPlayersConnected

        // if (webSocketData.bothPlayersConnected) {
        //     bothPlayersConnectedCallback()
        // } else {
        //     //TODO: Show message that both players are not connected
        // }

        if (hasGameEnded.value) {
            //TODO: Check winner using 'GameBoardHelper'
        }

        // everything above 4300 is an error and should be handled
        if (webSocketData.statusCode >= 4300) {
            console.error(`Error: ${webSocketData.statusCode}`)
        }

        // rematch was requested by me
        if (webSocketData.rematchRequestedByPlayer == playerNumber.value) {
            rematchRequested.value = true
        }

        // rematch was accepted by opponent
        if (webSocketData.statusCode == WebSocketCodes.REMATCH_ACCEPTED) { //TODO: Server should send this only after checking that both players have requested(/accepted) the rematch
            reset()

            const gameCode = webSocketData.gameCode
            init(gameCode)

            //TODO: No routing in store. Move this to the component
            router.push({path: "/multiplayer/play", query: {gameCode: gameCode}})

        }

    }

    const requestRematch = () => {
        const req: WebSocketRequest = {
            gameCode: gameCode.value ?? "",
            playerNumber: playerNumber.value,
            rematchRequestedByPlayer: playerNumber.value
        }
        sendWebSocketMsg(JSON.stringify(req))
    }

    function reset() {
        gameCode.value = null
        currentStatusCode.value = WebSocketCodes.STATUS_OK //TODO: Add client only status code for reset
        isMyMove.value = false
        gameBoard.value = [0, 0, 0, 0, 0, 0, 0, 0, 0]
        hasGameEnded.value = false
        rematchRequested.value = false
        bothPlayersConnected.value = false
        ws.value?.close()
    }

    return {
        init,
        reset,
        currentStatusCode,
        bothPlayersConnected,
        ws,
        gameCode,
        isMyMove,
        rematchRequested,
        gameBoard,
        hasGameEnded,
        requestRematch,
        sendGameBoardMove,

    }
})

import {type Ref, ref} from 'vue'
import {defineStore} from 'pinia'
import {getBaseURL} from "@/getBaseURL";
import {WebSocketCodes} from "@/StatusCodes";
import router from "@/router";
import type WebSocketRequest from "@/types/WebSocketRequest";
import type WebSocketResponse from "@/types/WebSocketResponse";
import {checkDraw, checkWinner} from "@/helper/GameBoardHelper";

export const useWebSocketStore = defineStore('websocket', () => {
    const ws = ref<WebSocket | null>(null)

    const gameCode: Ref<null | string> = ref(null)
    const currentStatusCode: Ref<number> = ref(WebSocketCodes.STATUS_OK)
    const userMessage: Ref<string> = ref("awaiting_opponent")

    const bothPlayersConnected = ref(false)
    const opponentDisconnected = ref(true)

    const playerNumber = ref(0)
    const isMyMove = ref(false)
    const gameBoard: Ref<number[]> = ref([0, 0, 0, 0, 0, 0, 0, 0, 0])
    const hasGameEnded = ref(false)

    const rematchRequested = ref(false)
    // const opponentDisconnectedBus = useEventBus<boolean>('opponentDisconnected')

    const init = (newGameCode: string) => {
        gameCode.value = newGameCode
        userMessage.value = "awaiting_opponent"
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
        currentStatusCode.value = webSocketData.statusCode

        playerNumber.value = webSocketData.playerNumber;
        isMyMove.value = (webSocketData.awaitMoveFromPlayer == playerNumber.value)
        console.log("Player number: ", playerNumber.value, "Awaiting move by: ", webSocketData.awaitMoveFromPlayer)
        console.log("Is my move: ", isMyMove.value)
        gameBoard.value = webSocketData.gameBoard
        hasGameEnded.value = webSocketData.hasGameEnded
        console.log("Both players connected: ", webSocketData.bothPlayersConnected)

        // Opponent disconnected during the game
        if (!webSocketData.bothPlayersConnected && bothPlayersConnected.value) {
            handleOpponentDisconnected()
        }

        bothPlayersConnected.value = webSocketData.bothPlayersConnected
        opponentDisconnected.value = !webSocketData.bothPlayersConnected

        updateUserMessage(webSocketData.statusCode)
        updateUserErrorMessage(webSocketData)


        // // rematch was requested by me
        // if (webSocketData.rematchRequestedByPlayer == playerNumber.value) {
        //     rematchRequested.value = true
        // }
        //
        // // rematch was accepted by opponent
        // if (webSocketData.statusCode == WebSocketCodes.REMATCH_ACCEPTED) { //TODO: Server should send this only after checking that both players have requested(/accepted) the rematch
        //     reset()
        //
        //     const gameCode = webSocketData.gameCode
        //     init(gameCode)
        //
        //     //TODO: No routing in store. Move this to the component
        //     router.push({path: "/multiplayer/play", query: {gameCode: gameCode}})
        //
        // }

    }

    /**
     * Updates the user message based on the given status code.
     *
     * Executes when the moving player changes, the game ends, or the game starts.
     * The user message is used to display the current state of the game to the user.
     *
     * @param {number} statusCode - The status code indicating the current state of the game.
     */
    function updateUserMessage(statusCode: number) {
        if (statusCode == WebSocketCodes.GAME_STARTED) {
            if (isMyMove.value) {
                userMessage.value = "your_turn"
            } else {
                userMessage.value = "opponents_turn"
            }
        }
        if (statusCode == WebSocketCodes.MOVE_ACCEPTED) {
            userMessage.value = "opponents_turn"
            isMyMove.value = false
        }
        if (statusCode == WebSocketCodes.YOUR_MOVE) {
            userMessage.value = "your_turn"
            isMyMove.value = true
        }
        if (statusCode == WebSocketCodes.GAME_ENDED || hasGameEnded.value) {
            if (checkDraw(gameBoard.value)) {
                userMessage.value = "game_draw"
            } else if (checkWinner(gameBoard.value) == playerNumber.value) {
                userMessage.value = "you_won"
            } else {
                userMessage.value = "you_lost"
            }
        }
    }

    function handleOpponentDisconnected() {
        opponentDisconnected.value = true
    }

    function updateUserErrorMessage(webSocketData: WebSocketResponse) {
        // TODO: Implement error handling
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
        currentStatusCode.value = WebSocketCodes.STATUS_OK
        userMessage.value = "awaiting_opponent"
        isMyMove.value = false
        gameBoard.value = [0, 0, 0, 0, 0, 0, 0, 0, 0]
        hasGameEnded.value = false
        rematchRequested.value = false
        bothPlayersConnected.value = false
        opponentDisconnected.value = true
        ws.value?.close()
    }

    return {
        init,
        reset,
        currentStatusCode,
        userMessage,
        bothPlayersConnected,
        opponentDisconnected,
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

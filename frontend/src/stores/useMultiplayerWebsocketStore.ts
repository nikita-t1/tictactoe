import {computed, type Ref, ref} from 'vue'
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

    // Contains the player number of the player who requested a rematch (and the opponents number if the opponent accepted the rematch)
    const rematchRequest = ref<number[]>([])
    const rematchRequestedByMe = computed(() => rematchRequest.value.some((player) => player == playerNumber.value))
    const rematchRequestedByOpponent = computed(() => rematchRequest.value.some((player) => player != playerNumber.value))
    const rematchText = computed(() => {
        if (rematchRequestedByMe.value && rematchRequestedByOpponent.value) {
            return "rematch_accepted"
        } else if (rematchRequestedByMe.value) {
            return "rematch_requested"
        } else if (rematchRequestedByOpponent.value) {
            return "opponent_requested_rematch"
        } else {
            return "request_rematch"
        }
    })
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
            rematchRequestedByPlayer: false
        }
        sendWebSocketMsg(JSON.stringify(req))
    }

    const onMessageEvent = (event: MessageEvent) => {
        const webSocketData: WebSocketResponse = JSON.parse(event.data)
        currentStatusCode.value = webSocketData.statusCode

        // Update the store with the received data
        playerNumber.value = webSocketData.playerNumber;
        isMyMove.value = (webSocketData.awaitMoveFromPlayer == playerNumber.value)
        gameBoard.value = webSocketData.gameBoard
        hasGameEnded.value = webSocketData.hasGameEnded

        // Opponent disconnected during the game
        // if (!webSocketData.bothPlayersConnected && bothPlayersConnected.value) {
        //     handleOpponentDisconnected()
        // }

        // Update the store with the received data
        bothPlayersConnected.value = webSocketData.bothPlayersConnected
        opponentDisconnected.value = !webSocketData.bothPlayersConnected

        // Update the user message based on the received data
        updateUserMessage(webSocketData.statusCode)
        // Update the user error message based on the received data
        updateUserErrorMessage(webSocketData)

        if (webSocketData.statusCode == WebSocketCodes.REMATCH_REQUESTED || webSocketData.statusCode == WebSocketCodes.REMATCH_ACCEPTED) {
            handleRematch(webSocketData)
        }

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

    function updateUserErrorMessage(webSocketData: WebSocketResponse) {
        // TODO: Implement error handling
    }

    function handleRematch(webSocketData: WebSocketResponse) {
        console.log("handleRematch")
        console.log("rematchRequest", rematchRequest.value)
        // correct button text will be evaluated based on this, so no extra code is required to tell that the opponent requested a rematch
        rematchRequest.value = webSocketData.rematchRequestedByPlayer
        console.log("rematchRequest", rematchRequest.value)

        // const isRematchRequestCode = webSocketData.statusCode == WebSocketCodes.REMATCH_REQUESTED
        // const requestedByOpponent = rematchRequestedByMe.value == false
        // if (isRematchRequestCode && requestedByOpponent) {
        //     // Opponent requested a rematch
        //     // show a dialog to accept or decline the rematch
        //     return
        // }

        const isRematchAcceptedCode = webSocketData.statusCode == WebSocketCodes.REMATCH_ACCEPTED
        console.log("isRematchAcceptedCode", isRematchAcceptedCode)
        if (isRematchAcceptedCode) {
            console.log("Both players have requested a rematch")
            // Both players have requested a rematch
            // reset the game and start a new game
            reset()
            const gameCode = webSocketData.gameCode
            init(gameCode)
            router.push({path: "/multiplayer/play", query: {gameCode: gameCode}})
        }
    }

    const requestRematch = () => {
        const req: WebSocketRequest = {
            gameCode: gameCode.value ?? "",
            playerNumber: playerNumber.value,
            rematchRequestedByPlayer: true
        }
        sendWebSocketMsg(JSON.stringify(req))
    }

    function reset() {
        ws.value?.close()
        gameCode.value = null
        currentStatusCode.value = WebSocketCodes.STATUS_OK
        userMessage.value = "awaiting_opponent"
        isMyMove.value = false
        gameBoard.value = [0, 0, 0, 0, 0, 0, 0, 0, 0]
        hasGameEnded.value = false
        rematchRequest.value = []
        bothPlayersConnected.value = false
        opponentDisconnected.value = true

    }

    return {
        init,
        reset,
        currentStatusCode,
        userMessage,
        rematchRequestedByOpponent,
        rematchRequestedByMe,
        rematchText,
        requestRematch,
        bothPlayersConnected,
        opponentDisconnected,
        ws,
        gameCode,
        isMyMove,
        gameBoard,
        hasGameEnded,
        sendGameBoardMove,

    }
})

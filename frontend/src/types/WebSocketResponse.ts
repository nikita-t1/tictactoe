export default interface WebSocketResponse {
    gameCode: string
    statusCode: number
    playerNumber: number
    gameBoard: number[]
    hasGameEnded: boolean
    awaitMoveFromPlayer: number
    bothPlayersConnected: boolean
    rematchRequestedByPlayer: number // rematch accepted only as status code
}

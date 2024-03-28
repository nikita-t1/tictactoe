export default interface WebSocketRequest {
    gameCode: string
    playerNumber: number
    playerMoveAtIndex?: number
    rematchRequestedByPlayer: boolean
}

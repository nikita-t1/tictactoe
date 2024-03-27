package dev.nikitatarasov.model

import kotlinx.serialization.Serializable

/**
 * Represents a WebSocket request sent from the client to the server.
 *
 * @param playerNumber The player number. An integer representing the player.
 * @param gameCode The game code. A string representing the unique ID of the game.
 * @param playerMoveAtIndex The index of the player move. An integer representing the index where the player made a move. One-based index from 1 to 9.
 * @param rematchRequestedByPlayer Indicates if the rematch is requested by the player. A boolean value.
 */
@Serializable
data class WebSocketRequest(
    val playerNumber: Int,
    val gameCode: String,
    val playerMoveAtIndex: Int? = null,
    val rematchRequestedByPlayer: Boolean = false,
)

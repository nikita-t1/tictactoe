package dev.nikitatarasov.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequest(
    val playerNumber: Int,
    val gameCode: String = "",
    val playerMoveAtIndex: Int = -1,
    val rematchRequestedByPlayer: Boolean = false,
)

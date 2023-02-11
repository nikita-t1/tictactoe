package dev.nikitatarasov.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WebSocketDataCode(
    val statusCode: StatusCode,
    val msg: String = ""
) {

    companion object {
        @Serializable
        enum class StatusCode {
            @SerialName("4000") STATUS_OK,
            @SerialName("4001") SECOND_PLAYER_CONNECTED,
            @SerialName("4002") MOVE_ACCEPTED,
            @SerialName("4003") YOUR_MOVE,
            @SerialName("4004") OPPONENT_MOVE,
            @SerialName("4005") GAME_BOARD,
            @SerialName("4008") YOU_WON,
            @SerialName("4009") OPPONENT_WON,
            @SerialName("4010") NOT_YOUR_TURN,
            @SerialName("4011") MOVE_INVALID,
            @SerialName("4301") NO_SECOND_PLAYER_YET,
            @SerialName("4501") GAME_CODE_TIMEOUT_REACHED,
            @SerialName("4502") GAME_ALREADY_HAS_TWO_PLAYERS,
            @SerialName("4503") NO_GAMECODE,
            @SerialName("4999") GENERIC_EXCEPTION
        }
    }
}

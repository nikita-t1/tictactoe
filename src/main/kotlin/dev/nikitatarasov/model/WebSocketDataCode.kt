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
            @SerialName("4001") GAME_BOARD,

            @SerialName("4101") FIRST_PLAYER_CONNECTED,
            @SerialName("4102") SECOND_PLAYER_CONNECTED,
            @SerialName("4103") YOUR_MOVE,
            @SerialName("4104") OPPONENT_MOVE,
            @SerialName("4105") REQUEST_REMATCH,
            @SerialName("4106") REMATCH_REQUESTED,
            @SerialName("4107") ACCEPT_REMATCH,
            @SerialName("4108") REMATCH_ACCEPTED,

            @SerialName("4200") MOVE_ACCEPTED,
            @SerialName("4201") YOU_WON,
            @SerialName("4202") OPPONENT_WON,

            @SerialName("4301") NO_FIRST_PLAYER_YET,
            @SerialName("4302") NO_SECOND_PLAYER_YET,
            @SerialName("4303") OPPONENT_DISCONNECTED,

            @SerialName("4401") NOT_YOUR_TURN,
            @SerialName("4402") MOVE_INVALID,
            @SerialName("4403") GAME_ALREADY_HAS_TWO_PLAYERS,
            @SerialName("4504") NO_GAMECODE_IN_QUERY,
            @SerialName("4505") NO_GAME_WITH_THIS_CODE_FOUND,

            @SerialName("4501") GAME_CODE_TIMEOUT_REACHED,
            @SerialName("4599") GENERIC_EXCEPTION
        }
    }
}

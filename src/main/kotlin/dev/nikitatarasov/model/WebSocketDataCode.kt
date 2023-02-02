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
        enum class StatusCode(code: Int) {
            @SerialName("4000") STATUS_OK(4000),
            @SerialName("4001") SECOND_PLAYER_CONNECTED(4001),
            @SerialName("4002") MOVE_ACCEPTED(4002),
            @SerialName("4003") YOUR_MOVE(4003),
            @SerialName("4004") OPPONENT_MOVE(4004),
            @SerialName("4005") GAME_BOARD(4005),
            @SerialName("4008") YOU_WON(4008),
            @SerialName("4009") OPPONENT_WON(4009),
            @SerialName("4010") NOT_YOUR_TURN(4010),
            @SerialName("4011") MOVE_INVALID(4011),
            @SerialName("4301") NO_SECOND_PLAYER_YET(4301),
            @SerialName("4501") GAME_CODE_TIMEOUT_REACHED(4501),
            @SerialName("4502") GAME_ALREADY_HAS_TWO_PLAYERS(4502),
            @SerialName("4503") NO_GAMECODE(4503)
        }
    }
}

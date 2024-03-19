package dev.nikitatarasov.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("MagicNumber")
@Serializable
enum class StatusCode(val code: Int) {
    @SerialName("4000") STATUS_OK(4000),
//    @SerialName("4001") GAME_BOARD(4001),

    @SerialName("4100") BOTH_PLAYERS_CONNECTED(4100),
//    @SerialName("4101") FIRST_PLAYER_CONNECTED(4101),
//    @SerialName("4102") SECOND_PLAYER_CONNECTED(4102),
    @SerialName("4103") YOUR_MOVE(4103),
//    @SerialName("4104") OPPONENT_MOVE(4104),
//    @SerialName("4105") REQUEST_REMATCH(4105),
//    @SerialName("4106") REMATCH_REQUESTED(4106),
//    @SerialName("4107") ACCEPT_REMATCH(4107),
//    @SerialName("4108") REMATCH_ACCEPTED(4108),

    @SerialName("4200") MOVE_ACCEPTED(4200),
//    @SerialName("4201") YOU_WON(4201),
//    @SerialName("4202") OPPONENT_WON(4202),
//    @SerialName("4203") GAME_ENDED_IN_DRAW(4203),
    @SerialName("4204") GAME_ENDED(4204), // winner will be evaluated by the client

    @SerialName("4300") NO_OPPONENT_YET(4300),
//    @SerialName("4301") NO_FIRST_PLAYER_YET(4301),
//    @SerialName("4302") NO_SECOND_PLAYER_YET(4302),
    @SerialName("4303") OPPONENT_DISCONNECTED(4303),

    @SerialName("4401") NOT_YOUR_TURN(4401),
    @SerialName("4402") INVALID_MOVE(4402),
    @SerialName("4403") GAME_ALREADY_HAS_TWO_PLAYERS(4403),
    @SerialName("4504") NO_GAMECODE_IN_QUERY(4504),
    @SerialName("4505") NO_GAME_WITH_THIS_CODE_FOUND(4505),

    @SerialName("4501") GAME_CODE_TIMEOUT_REACHED(4501),
    @SerialName("4599") GENERIC_EXCEPTION(4599);
}

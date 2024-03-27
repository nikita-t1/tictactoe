package dev.nikitatarasov.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Suppress("MagicNumber")
@Serializable
enum class StatusCode(val code: Int) {

    /**
     * These codes represent the initial state of the game.
    */
    @SerialName("4000") STATUS_OK(4000),
    @SerialName("4001") GAME_STARTED(4001),
    @SerialName("4002") BOTH_PLAYERS_CONNECTED(4002),

    /**
     * These codes are related to the gameplay and determining the game's end
    */
    @SerialName("4200") YOUR_MOVE(4200),
    @SerialName("4201") MOVE_ACCEPTED(4201),
    @SerialName("4203") GAME_ENDED(4204), // winner will be evaluated by the client

    /**
     * These codes handle the process of requesting and accepting a rematch
    */
    @SerialName("4300") REMATCH_REQUESTED(4300),
    @SerialName("4301") REMATCH_ACCEPTED(4301),

    /**
     * These codes are used to address issues with the opponent or the game itself
    */
    @SerialName("4400") NO_OPPONENT_YET(4400),
    @SerialName("4401") OPPONENT_DISCONNECTED(4401),
    @SerialName("4411") NOT_YOUR_TURN(4411),
    @SerialName("4412") INVALID_MOVE(4412),
    @SerialName("4413") GAME_ALREADY_HAS_TWO_PLAYERS(4413),
    @SerialName("4514") NO_GAMECODE_IN_QUERY(4514),
    @SerialName("4515") NO_GAME_WITH_THIS_CODE_FOUND(4515),

    /**
     * These codes manage unique gamecode scenarios and general exceptions
    */
    @SerialName("4501") GAME_CODE_TIMEOUT_REACHED(4501),
    @SerialName("4599") GENERIC_EXCEPTION(4599);
}

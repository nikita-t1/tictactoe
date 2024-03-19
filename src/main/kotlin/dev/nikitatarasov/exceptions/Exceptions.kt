package dev.nikitatarasov.exceptions

import dev.nikitatarasov.model.StatusCode


class NoGameCodeException(
    message: String? = "No GameCode was provided as Query Parameter, but one is required",
    webSocketDataCode: StatusCode = StatusCode.NO_GAMECODE_IN_QUERY
) : WebSocketException(message, webSocketDataCode)

class TimeoutSecondPlayerException(
    message: String? = "Timeout was reached before the second Player joined the Game",
    webSocketDataCode: StatusCode = StatusCode.GAME_CODE_TIMEOUT_REACHED
) : WebSocketException(message, webSocketDataCode)

class GameLobbyIsFullException(
    message: String? = "The Game with this Code already has 2 Players",
    webSocketDataCode: StatusCode = StatusCode.GAME_ALREADY_HAS_TWO_PLAYERS
) : WebSocketException(message)

open class WebSocketException(
    message: String? = null,
    val webSocketDataCode: StatusCode = StatusCode.GENERIC_EXCEPTION,
    cause: Throwable? = null
) : Exception(message, cause)

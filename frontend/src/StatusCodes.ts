enum WebSocketCodes {

    STATUS_OK = 4000,
    GAME_BOARD = 4001,
    GAME_STARTED = 4002,

    FIRST_PLAYER_CONNECTED = 4101,
    SECOND_PLAYER_CONNECTED = 4102,
    YOUR_MOVE = 4103,
    OPPONENT_MOVE = 4104,
    REQUEST_REMATCH = 4105,
    REMATCH_REQUESTED = 4106,
    ACCEPT_REMATCH = 4107,
    REMATCH_ACCEPTED = 4108,

    MOVE_ACCEPTED = 4200,
    YOU_WON = 4201,
    OPPONENT_WON = 4202,
    GAME_ENDED_IN_DRAW = 4203,
    GAME_ENDED = 4203,

    NO_OPPONENT_YET = 4300,
    NO_FIRST_PLAYER_YET = 4301,
    NO_SECOND_PLAYER_YET = 4302,
    OPPONENT_DISCONNECTED = 4303,

    NOT_YOUR_TURN = 4401,
    MOVE_INVALID = 4402,
    GAME_ALREADY_HAS_TWO_PLAYERS = 4403,
    NO_GAMECODE_IN_QUERY = 4504,
    NO_GAME_WITH_THIS_CODE_FOUND = 4505,

    GAME_CODE_TIMEOUT_REACHED = 4501,
    GENERIC_EXCEPTION = 4599,
}

enum ErrorCodes {
    GENERIC_ERROR = 600,
    WEBSOCKET_IS_NULL = 601
}

export { WebSocketCodes, ErrorCodes}

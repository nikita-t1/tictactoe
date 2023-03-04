enum WebSocketCodes {

    STATUS_OK = 4000,
    GAME_BOARD = 4001,

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

const MessageMap = new Map()

MessageMap.set(WebSocketCodes.STATUS_OK, `STATUS_OK`)
MessageMap.set(WebSocketCodes.GAME_BOARD, `GAME_BOARD`)

MessageMap.set(WebSocketCodes.FIRST_PLAYER_CONNECTED, `The first Player has joined the Game`)
MessageMap.set(WebSocketCodes.SECOND_PLAYER_CONNECTED, `The second Player has joined the Game`)
MessageMap.set(WebSocketCodes.YOUR_MOVE, `It's your turn`)
MessageMap.set(WebSocketCodes.OPPONENT_MOVE, `It's your opponent's turn now`)
MessageMap.set(WebSocketCodes.REMATCH_REQUESTED, `Your opponent requested a rematch`)
MessageMap.set(WebSocketCodes.REMATCH_ACCEPTED, `Your opponent accepted the rematch`)

MessageMap.set(WebSocketCodes.MOVE_ACCEPTED, `This move was valid`)
MessageMap.set(WebSocketCodes.YOU_WON, `You are the Winner`)
MessageMap.set(WebSocketCodes.OPPONENT_WON, `Sorry, you lost the Game`)
MessageMap.set(WebSocketCodes.GAME_ENDED_IN_DRAW, `The game ended in a draw`)

MessageMap.set(WebSocketCodes.NO_SECOND_PLAYER_YET, `No second Player has joined this Game yet`)
MessageMap.set(WebSocketCodes.OPPONENT_DISCONNECTED, `Your opponent has left the Game`)

MessageMap.set(WebSocketCodes.NOT_YOUR_TURN, `It's not your turn. Be patient`)
MessageMap.set(WebSocketCodes.MOVE_INVALID, `This move is not allowed`)
MessageMap.set(WebSocketCodes.GAME_ALREADY_HAS_TWO_PLAYERS, `The Game with this Code already has 2 Players`)
MessageMap.set(WebSocketCodes.NO_GAMECODE_IN_QUERY, `No GameCode was send with this request`)
MessageMap.set(WebSocketCodes.NO_GAME_WITH_THIS_CODE_FOUND, `No Game with this Code was found`)

MessageMap.set(WebSocketCodes.GAME_CODE_TIMEOUT_REACHED, `Timeout reached before second player connected`)
MessageMap.set(WebSocketCodes.GENERIC_EXCEPTION, `Generic Exception`)

//////////////////////////////////////////////////

MessageMap.set(ErrorCodes.GENERIC_ERROR, `Generic Exception`)
MessageMap.set(ErrorCodes.WEBSOCKET_IS_NULL, `The Websocket Connection seems to be null`)


export { WebSocketCodes, ErrorCodes, MessageMap}

package lmu.msp.backend.socket.model

enum class MessageType {
    CONNECT,
    DISCONNECT,
    CHAT_MESSAGE,
    GROUP_MESSAGE,
    DRAW_PATH,
    DRAW_IMAGE,
    DRAW_RESET,
    INITIATIVE_ADD,
    INITIATIVE_REST
}
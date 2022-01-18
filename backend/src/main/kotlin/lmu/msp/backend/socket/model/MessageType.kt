package lmu.msp.backend.socket.model

enum class MessageType {
    CONNECT,
    DISCONNECT,
    CHAT_MESSAGE,
    DRAW_PATH,
    DRAW_IMAGE,
    DRAW_RESET
}
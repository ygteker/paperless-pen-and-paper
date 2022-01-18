package lmu.msp.frontend.models.websocket

enum class MessageType {
    CONNECT,
    DISCONNECT,
    CHAT_MESSAGE,
    DRAW_PATH,
    DRAW_IMAGE,
    DRAW_RESET
}
package lmu.msp.frontend.models.websocket

/**
 * enumeration class for all websocket message types
 *
 */
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
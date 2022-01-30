package lmu.msp.frontend.api.model

data class GeneralChatMessage (
    val from: String,
    val message: String,
    val type: ChatType,
    var self: Boolean
)
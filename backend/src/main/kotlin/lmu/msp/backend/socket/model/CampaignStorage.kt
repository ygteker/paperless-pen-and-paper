package lmu.msp.backend.socket.model

class CampaignStorage {
    val chatMessage: MutableList<ChatMessage> = mutableListOf()
    val drawMessage: MutableList<DrawMessage> = mutableListOf()
    val groupMessage: MutableList<GroupMessage> = mutableListOf()
    var drawImage: DrawImage = DrawImage()
}
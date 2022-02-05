package lmu.msp.backend.socket.model


/**
 * quick & dirty implementation of a storage/cache (this is the data obj of that storage)
 * if there would be more time this would be converted into a repository with models like the data from the rest calls
 *
 */
class CampaignStorage {
    val chatMessage: MutableList<ChatMessage> = mutableListOf()
    val drawMessage: MutableList<DrawMessage> = mutableListOf()
    val groupMessage: MutableList<GroupMessage> = mutableListOf()
    var drawImage: DrawImage = DrawImage()
}
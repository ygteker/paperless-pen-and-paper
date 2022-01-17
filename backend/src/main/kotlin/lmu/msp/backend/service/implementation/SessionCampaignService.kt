package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.User
import lmu.msp.backend.service.ISessionCampaignService
import lmu.msp.backend.service.ISessionService
import lmu.msp.backend.service.IUserService
import lmu.msp.backend.socket.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SessionCampaignService(
    @Autowired private val sessionService: ISessionService.ISessionWorkerService,
    @Autowired private val userService: IUserService
) : ISessionCampaignService {

    private val campaignMap = HashMap<Long, CampaignStorage>()

    private fun handleChatMessage(auth0Id: String, campaignId: Long, messages: List<ChatMessage>?) {
        val user = userService.getUserByAuth0Id(auth0Id)
        messages?.forEach { chatMsg ->
            val userToSend = userService.getUserById(chatMsg.receiverId)
            userToSend?.let {
                //create a new message. otherwise, the sender of the message can fake the senderId
                val msg = ChatMessage(user.id, chatMsg.receiverId, chatMsg.message)

                campaignMap[campaignId]!!.chatMessage.add(msg)

                sessionService.sendTo(
                    BaseMessage(MessageType.CHAT_MESSAGE, listOf(msg)),
                    campaignId,
                    userToSend.auth0Id
                )
            }
        }

    }

    private fun handleDraw(campaignId: Long, drawMessages: List<DrawMessage>?) {
        if (drawMessages != null) {
            campaignMap[campaignId]!!.drawMessage.addAll(drawMessages)
            sessionService.sendTo(BaseMessage(MessageType.DRAW_PATH, null, drawMessages), campaignId)
        }
    }

    private fun handleResetDraw(campaignId: Long) {
        campaignMap[campaignId]!!.drawMessage.clear()
        sessionService.sendTo(BaseMessage(MessageType.DRAW_RESET), campaignId)
    }

    private fun filteredBasedMessages(campaignId: Long, user: User): List<ChatMessage> =
        campaignMap[campaignId]?.chatMessage?.filter { chatMessage -> chatMessage.receiverId == user.id || chatMessage.senderId == user.id }
            ?: emptyList()


    override fun handleMessage(auth0Id: String, campaignId: Long, baseMessage: BaseMessage) {
        if (!campaignMap.containsKey(campaignId)) {
            campaignMap[campaignId] = CampaignStorage()
        }

        when (baseMessage.messageType) {
            MessageType.CONNECT -> return //this message type is only send from the server to a newly connected client
            MessageType.DISCONNECT -> TODO()
            MessageType.CHAT_MESSAGE -> handleChatMessage(auth0Id, campaignId, baseMessage.chatMessage)
            MessageType.DRAW_PATH -> handleDraw(campaignId, baseMessage.drawMessage)
            MessageType.DRAW_IMAGE -> TODO()
            MessageType.DRAW_RESET -> handleResetDraw(campaignId)
        }
    }

    override fun connected(auth0Id: String, campaignId: Long) {
        val user = userService.getUserByAuth0Id(auth0Id)

        val messages = filteredBasedMessages(campaignId, user)
        val paths = campaignMap[campaignId]?.drawMessage ?: emptyList()

        val baseMessage = BaseMessage(MessageType.CONNECT, messages, paths)

        sessionService.sendTo(baseMessage, campaignId, auth0Id)
    }

    override fun disconnected(auth0Id: String, campaignId: Long) {
        TODO("Not yet implemented")
    }
}
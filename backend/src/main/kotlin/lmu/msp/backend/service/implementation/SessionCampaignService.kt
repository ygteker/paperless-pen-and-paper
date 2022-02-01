package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.User
import lmu.msp.backend.service.ISessionCampaignService
import lmu.msp.backend.service.ISessionService
import lmu.msp.backend.service.IUserService
import lmu.msp.backend.socket.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/**
 * implementation of ISessionCampaignService
 * contains the "business logic"
 *
 * @property sessionService
 * @property userService
 */
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

    private fun handleDraw(auth0Id: String, campaignId: Long, drawMessages: List<DrawMessage>?) {
        if (drawMessages != null) {
            campaignMap[campaignId]!!.drawMessage.addAll(drawMessages)
            //don't send to yourself
            sessionService.sendToFiltered(BaseMessage(MessageType.DRAW_PATH, null, drawMessages), campaignId, auth0Id)
        }
    }

    private fun handleResetDraw(campaignId: Long) {
        campaignMap[campaignId]!!.drawMessage.clear()
        sessionService.sendTo(BaseMessage(MessageType.DRAW_RESET), campaignId)
    }

    private fun filteredBasedMessages(campaignId: Long, user: User): List<ChatMessage> =
        campaignMap[campaignId]?.chatMessage?.filter { chatMessage -> chatMessage.receiverId == user.id || chatMessage.senderId == user.id }
            ?: emptyList()


    private fun handleDrawImage(auth0Id: String, campaignId: Long, drawImage: DrawImage?) {
        if (drawImage != null) {
            println("Recived an image ${drawImage.imageBase64}")
            campaignMap[campaignId]!!.drawImage = drawImage
            sessionService.sendToFiltered(
                BaseMessage(MessageType.DRAW_IMAGE, null, null, null, drawImage),
                campaignId,
                auth0Id
            )
        }
    }

    private fun handleGroupMessage(auth0Id: String, campaignId: Long, groupMessage: List<GroupMessage>?) {
        groupMessage?.forEach {
            val message = GroupMessage(userService.getUserByAuth0Id(auth0Id).id, it.message)
            campaignMap[campaignId]!!.groupMessage.add(message)
            sessionService.sendTo(BaseMessage(MessageType.GROUP_MESSAGE, null, null, listOf(message), null), campaignId)
        }
    }


    override fun handleMessage(auth0Id: String, campaignId: Long, baseMessage: BaseMessage) {
        if (!campaignMap.containsKey(campaignId)) {
            campaignMap[campaignId] = CampaignStorage()
        }

        when (baseMessage.messageType) {
            MessageType.CONNECT -> return //this message type is only send from the server to a newly connected client
            MessageType.DISCONNECT -> return //nothing at the moment
            MessageType.CHAT_MESSAGE -> handleChatMessage(auth0Id, campaignId, baseMessage.chatMessage)
            MessageType.DRAW_PATH -> handleDraw(auth0Id, campaignId, baseMessage.drawMessage)
            MessageType.DRAW_IMAGE -> handleDrawImage(auth0Id, campaignId, baseMessage.drawImage)
            MessageType.DRAW_RESET -> handleResetDraw(campaignId)
            MessageType.GROUP_MESSAGE -> handleGroupMessage(auth0Id, campaignId, baseMessage.groupMessage)
            MessageType.INITIATIVE_ADD -> return //nothing at the moment -> for future feature
            MessageType.INITIATIVE_REST -> return //nothing at the moment -> for future feature
        }
    }

    /**
     * send all current session information to the user
     *
     * @param auth0Id
     * @param campaignId
     */
    override fun connected(auth0Id: String, campaignId: Long) {
        val user = userService.getUserByAuth0Id(auth0Id)

        val messages = filteredBasedMessages(campaignId, user)
        val paths = campaignMap[campaignId]?.drawMessage ?: emptyList()
        val groupMessage = campaignMap[campaignId]?.groupMessage ?: emptyList()
        val image = campaignMap[campaignId]?.drawImage ?: DrawImage()

        val baseMessage = BaseMessage(MessageType.CONNECT, messages, paths, groupMessage, image)

        sessionService.sendTo(baseMessage, campaignId, auth0Id)
    }


    /**
     * nothing to do at the moment
     *
     * @param auth0Id
     * @param campaignId
     */
    override fun disconnected(auth0Id: String, campaignId: Long) {
    }
}
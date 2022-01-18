package lmu.msp.backend.service

import lmu.msp.backend.socket.model.BaseMessage
import org.springframework.web.socket.WebSocketSession

/**
 * Services important to manage a websocket session
 *
 */
interface ISessionService {

    /**
     * interface of the service that only show methods to work with the session (not to manage)
     *
     */
    interface ISessionWorkerService {
        /**
         * broadcast a message to every auth0Id in a campaign
         *
         * @param message
         * @param campaignId
         * @param auth0Ids vararg. if empty send message to everyone in the campaign (also to yourself)
         */
        fun sendTo(
            message: BaseMessage,
            campaignId: Long,
            vararg auth0Ids: String
        )

        /**
         * broadcast a message to every auth0Id in a campaign
         *
         * @param message
         * @param campaignId
         * @param auth0IdsToIgnore the message will not be send to the auth0ids provided here
         */
        fun sendToFiltered(
            message: BaseMessage,
            campaignId: Long,
            vararg auth0IdsToIgnore: String
        )

        /**
         * get list of currently active session in the campaign
         *
         * @param campaignId
         * @return
         */
        fun getActive(campaignId: Long): Collection<String>
    }

    /**
     * manager of the session. Add and Remove Sessions from the "aktiv" list
     *
     */
    interface ISessionManagerService {

        /**
         * add a websocket to the map of currently managed sessions
         *
         * @param session
         * @return false if the session wasn't stored otherwise true.
         */
        fun add(session: WebSocketSession): Boolean

        /**
         * remove a websocket from the map of currently managed sessions
         *
         * @param session
         * @return false if session wasn't stored otherwise true
         */
        fun remove(session: WebSocketSession): Boolean
    }

}
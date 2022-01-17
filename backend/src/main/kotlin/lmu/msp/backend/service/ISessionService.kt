package lmu.msp.backend.service

import lmu.msp.backend.socket.model.BaseMessage
import org.springframework.web.socket.WebSocketSession

interface ISessionService {

    interface ISessionWorkerService {
        fun sendTo(message: BaseMessage, campaignId: Long, vararg auth0Ids: String)
        fun getActive(campaignId: Long): Collection<String>
    }

    interface ISessionManagerService {

        /**
         * add a websocket to the map of currently managed sessions
         * return false if the session wasn't stored
         *
         * @param session
         * @return
         */
        fun add(session: WebSocketSession): Boolean

        fun remove(session: WebSocketSession): Boolean
    }

}
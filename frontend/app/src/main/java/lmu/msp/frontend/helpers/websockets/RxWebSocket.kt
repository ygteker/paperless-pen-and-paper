package lmu.msp.frontend.helpers.websockets

import io.reactivex.Emitter
import io.reactivex.Observable
import okhttp3.*

class RxWebSocket(okHttpClient: OkHttpClient, request: Request) : IRxWebSocket {
    private lateinit var webSocket: WebSocket
    private val observable = Observable.create<String> {
        webSocket = okHttpClient.newWebSocket(request, RxWebSocketListener(it))
    }

    override fun close() {
        webSocket.cancel()
    }

    override fun getObserver(): Observable<String> {
        return observable
    }

    override fun send(message: String) {
        webSocket.send(message)
    }

    inner class RxWebSocketListener(private val emitter: Emitter<String>) : WebSocketListener() {
        /** Invoked when a text (type `0x1`) message has been received. */
        override fun onMessage(webSocket: WebSocket, text: String) {
            emitter.onNext(text)
        }

        /**
         * Invoked when both peers have indicated that no more messages will be transmitted and the
         * connection has been successfully released. No further calls to this listener will be made.
         */
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            emitter.onComplete()
        }

        /**
         * Invoked when a web socket has been closed due to an error reading from or writing to the
         * network. Both outgoing and incoming messages may have been lost. No further calls to this
         * listener will be made.
         */
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            emitter.onError(RxWebSocketException(t, response))
        }
    }
}
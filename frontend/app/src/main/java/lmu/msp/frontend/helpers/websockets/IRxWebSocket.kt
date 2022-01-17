package lmu.msp.frontend.helpers.websockets

import io.reactivex.Observable

interface IRxWebSocket {

    fun getObserver(): Observable<String>
    fun send(message: String)
    fun close()
}
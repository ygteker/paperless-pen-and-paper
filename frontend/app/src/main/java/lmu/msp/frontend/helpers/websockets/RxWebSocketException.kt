package lmu.msp.frontend.helpers.websockets

import okhttp3.Response

class RxWebSocketException(throwable: Throwable, val response: Response?) : Exception(throwable) {
}
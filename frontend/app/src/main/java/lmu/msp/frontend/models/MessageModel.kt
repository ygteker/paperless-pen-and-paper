package lmu.msp.frontend.models

class MessageModel(title: String, summary: String, isRead: Boolean) {

    companion object{
        private var lastMessageModelId = 0
        fun createMessagesList(numberOfMessages: Int): ArrayList<MessageModel> {
            val messages = ArrayList<MessageModel>()
            for (i in 1..numberOfMessages) {

            }
        }
    }

}
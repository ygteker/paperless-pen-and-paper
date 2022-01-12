package lmu.msp.frontend.models

class MessageModel(val from: String, val title: String, val content: String, val isRead: Boolean) {


    companion object {
        fun createContactsList(amount: Int): ArrayList<MessageModel> {
            val messages = ArrayList<MessageModel>()
            for (i in 1..amount) {
                messages.add(MessageModel("Player B", "Sample message", "Sample Content", false))
            }
            return messages
        }
    }
}
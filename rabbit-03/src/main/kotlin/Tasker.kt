import kotlin.system.exitProcess

object Tasker {
    private const val QUEUE_NAME = "ack_queue"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        var index = 0
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        while (true) {
            val readLine = readLine()
            if (readLine != null) {
                if (readLine.uppercase() == "EOF") {
                    exitProcess(0)
                } else {
                    channel
                        .basicPublish("", QUEUE_NAME, null, "${index++}:$readLine".toByteArray())
                }
            }
        }
    }
}

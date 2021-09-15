import com.rabbitmq.client.MessageProperties
import kotlin.system.exitProcess

/**
 * 持久化队列
 */
object Tasker {
    private const val QUEUE_NAME = "durable_queue"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()

        channel.queueDeclare(QUEUE_NAME, true, false, false, null)
        while (true) {
            val readLine = readLine()
            if (readLine != null) {
                if (readLine.uppercase() == "EOF") {
                    exitProcess(0)
                } else {
                    channel
                        .basicPublish(
                            "", QUEUE_NAME,
                            // 消息持久化
                            MessageProperties.PERSISTENT_TEXT_PLAIN, readLine.toByteArray()
                        )
                }
            }
        }
    }
}

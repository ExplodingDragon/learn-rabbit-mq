import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset

object GlobalTopicWorker {

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare("logs", BuiltinExchangeType.TOPIC)
        val queue = channel.queueDeclare()
        channel.queueBind(queue.queue, "logs", "log.#")
        channel.basicConsume(
            queue.queue, true,
            { _: String, delivery: Delivery ->
                println("message:" + delivery.body.toString(Charset.defaultCharset()))
            },
            { _ ->
            }
        )
    }
}

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset

object Worker01 {

    private const val NAME = "logs"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare(NAME, "fanout")
        val queue = channel.queueDeclare()
        channel.queueBind(queue.queue, NAME, "")
        channel.basicConsume(
            queue.queue, true,
            DeliverCallback { _: String, delivery: Delivery ->
                println("message:" + delivery.body.toString(Charset.defaultCharset()))
            },
            CancelCallback {
                println("中断：$it")
            }
        )
    }
}

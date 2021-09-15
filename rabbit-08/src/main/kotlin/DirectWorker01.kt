import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset

object DirectWorker01 {

    private const val NAME = "direct_logs"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare(NAME, BuiltinExchangeType.DIRECT)
        val queue = channel.queueDeclare("console", false, false, false, null)
        channel.queueBind(queue.queue, NAME, "a")
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

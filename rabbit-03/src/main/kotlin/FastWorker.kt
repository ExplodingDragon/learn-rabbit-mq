import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset

object FastWorker {
    private const val QUEUE_NAME = "ack_queue"

    /**
     * 取消自动应答
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.basicConsume(
            QUEUE_NAME, false,
            DeliverCallback { _: String, delivery: Delivery ->
                val message = delivery.body.toString(Charset.defaultCharset())
                Thread.sleep(500)
                channel.basicAck(delivery.envelope.deliveryTag, false)
                println("message:$message")
            },
            CancelCallback {
                println("中断：$it")
            }
        )
    }
}

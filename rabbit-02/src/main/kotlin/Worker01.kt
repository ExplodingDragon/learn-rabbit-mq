import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset

object Worker01 {

    private const val QUEUE_NAME = "hello"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.basicConsume(
            QUEUE_NAME, true,
            DeliverCallback { _: String, delivery: Delivery ->
                println("message:" + delivery.body.toString(Charset.defaultCharset()))
            },
            CancelCallback {
                println("中断：$it")
            }
        )
    }
}

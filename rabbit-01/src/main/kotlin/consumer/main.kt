package consumer

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import java.nio.charset.Charset

const val QUEUE_NAME = "hello"

fun main() {
    val factory = ConnectionFactory()
    factory.run {
        host = "10.10.10.4"
        username = "dragon"
        password = "dragon"
    }
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.basicConsume(
        QUEUE_NAME, true,
        DeliverCallback { message: String, delivery: Delivery ->
            println(delivery.body.toString(Charset.defaultCharset()))
        },
        CancelCallback {
            println("中断：$it")
        }

    )
}

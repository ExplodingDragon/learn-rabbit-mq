import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import java.nio.charset.Charset

object DenyWorker {
    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare("normal_log", BuiltinExchangeType.DIRECT)
        channel.exchangeDeclare("dead_log", BuiltinExchangeType.DIRECT)

        channel.queueDeclare(
            "normal_queue", false, false, false,
            mapOf(
                Pair("x-dead-letter-exchange", "dead_log"),
                Pair("x-dead-letter-routing-key", "dead")
            )
        )

        channel.queueDeclare("dead_queue", false, false, false, null)
        channel.queueBind("normal_queue", "normal_log", "normal")
        channel.queueBind("dead_queue", "dead_log", "dead")
        channel.basicConsume(
            "normal_queue", false,
            DeliverCallback { _, message ->
                val msg = message.body.toString(Charset.defaultCharset())
                if (msg.endsWith("5")) {
                    channel.basicReject(message.envelope.deliveryTag, false)
                    println("拒绝消息：$msg")
                } else {
                    println("普通队列：$msg")
                }
            },
            CancelCallback {
            }
        )
        channel.basicConsume(
            "dead_queue", true,
            DeliverCallback { _, message ->
                println("死信队列：" + message.body.toString(Charset.defaultCharset()))
            },
            CancelCallback {
            }
        )
    }
}

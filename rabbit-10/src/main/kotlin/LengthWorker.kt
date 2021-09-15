import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.DeliverCallback
import java.nio.charset.Charset

object LengthWorker {
    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare("normal_log", BuiltinExchangeType.DIRECT)
        channel.exchangeDeclare("dead_log", BuiltinExchangeType.DIRECT)

        channel.queueDeclare(
            "normal_queue", false, false, false,
            mapOf(
                Pair("x-max-length", 10), // 最大长度
                Pair("x-dead-letter-exchange", "dead_log"),
                Pair("x-dead-letter-routing-key", "dead")
            )
        )

        channel.queueDeclare("dead_queue", false, false, false, null)
        channel.queueBind("normal_queue", "normal_log", "normal")
        channel.queueBind("dead_queue", "dead_log", "dead")
        channel.basicConsume(
            "normal_queue", true,
            DeliverCallback { _, message ->
                println("普通队列：" + message.body.toString(Charset.defaultCharset()))
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

import com.rabbitmq.client.BuiltinExchangeType
import kotlin.system.exitProcess

/**
 * 交换机 (发布、订阅)
 */
object TopicTasker {

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare("logs", BuiltinExchangeType.TOPIC)
        while (true) {
            val readLine = readLine()
            if (readLine != null) {
                val split = readLine.split(" ")
                if (readLine.uppercase() == "EOF" && split.size == 2) {
                    exitProcess(0)
                } else {
                    println("send ${split[1]} to ${split[0]}；")
                    channel
                        .basicPublish("logs", split[0], null, split[1].toByteArray())
                }
            }
        }
    }
}

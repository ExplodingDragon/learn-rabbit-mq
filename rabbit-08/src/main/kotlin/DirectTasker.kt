import com.rabbitmq.client.BuiltinExchangeType
import kotlin.system.exitProcess

/**
 * 交换机 (发布、订阅)
 */
object DirectTasker {
    private const val NAME = "direct_logs"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare(NAME, BuiltinExchangeType.DIRECT)
        while (true) {
            val readLine = readLine()
            if (readLine != null) {
                val split = readLine.split(" ")
                if (readLine.uppercase() == "EOF" && split.size == 2) {
                    exitProcess(0)
                } else {
                    channel
                        .basicPublish(NAME, split[0], null, split[1].toByteArray())
                }
            }
        }
    }
}

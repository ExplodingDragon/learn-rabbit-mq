import kotlin.system.exitProcess

/**
 * 交换机 (发布、订阅)
 */
object Tasker {
    private const val NAME = "logs"

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        channel.exchangeDeclare(NAME, "fanout")
        while (true) {
            val readLine = readLine()
            if (readLine != null) {
                if (readLine.uppercase() == "EOF") {
                    exitProcess(0)
                } else {
                    channel
                        .basicPublish(NAME, "", null, readLine.toByteArray())
                }
            }
        }
    }
}

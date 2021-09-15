

/**
 * 交换机 死信
 */
object DenyTasker {

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        for (i in 0..20) {
            channel
                .basicPublish("normal_log", "normal", null, "message: $i".toByteArray())
        }
    }
}

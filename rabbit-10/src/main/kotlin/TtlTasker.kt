import com.rabbitmq.client.AMQP

/**
 * 交换机 死信
 */
object TtlTasker {

    private val prop: AMQP.BasicProperties = AMQP.BasicProperties()
        .builder().expiration("10000").build()

    @JvmStatic
    fun main(args: Array<String>) {
        val channel = RabbitMQUtils.createChannel()
        for (i in 0..10) {
            channel
                .basicPublish("normal_log", "normal", prop, "message: $i".toByteArray())
        }
    }
}

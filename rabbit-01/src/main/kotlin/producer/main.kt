package producer

import com.rabbitmq.client.ConnectionFactory

const val QUEUE_NAME = "hello"

/**
 * 生产者
 */
fun main() {
    val factory = ConnectionFactory()
    factory.run {
        host = "10.10.10.4"
        username = "dragon"
        password = "dragon"
    }
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    val message = "hello world"
    for (i in 0..10) {
        channel.basicPublish("", QUEUE_NAME, null, "$message : $i".toByteArray())
    }
}

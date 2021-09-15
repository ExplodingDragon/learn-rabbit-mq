import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

object RabbitMQUtils {
    private val factory = ConnectionFactory()
    private var connection: Connection

    init {
        factory.run {
            host = "10.10.10.4"
            username = "guest"
            password = "guest"
        }
        connection = factory.newConnection()
    }

    fun createChannel(): Channel = connection.createChannel()
}

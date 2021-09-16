package i.test.hooks

import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class ConfirmQueueConsumer(private val rabbitTemplate: RabbitTemplate) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = ["priority.queue"])
    fun receiveD(msg: Message, channel: Channel) {
        val data = msg.body.toString(Charset.defaultCharset())
        logger.info("队列收到消息，消息内容：{}", data)
    }
}

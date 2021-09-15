package i.test.hooks

import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.util.Date

@Component
class PluginQueueConsumer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RabbitListener(queues = ["delayed.queue"])
    fun receiveD(msg: Message, channel: Channel) {
        val data = msg.body.toString(Charset.defaultCharset())
        logger.info("收到消息，当前时间：{}，消息内容：{}", Date(), data)
    }
}

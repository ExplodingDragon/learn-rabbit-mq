package i.test.hooks

import com.rabbitmq.client.Channel
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.ReturnedMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import javax.annotation.PostConstruct

@Component
class ConfirmQueueConsumer(private val rabbitTemplate: RabbitTemplate) :
    RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostConstruct
    fun init() {
        rabbitTemplate.setConfirmCallback(this)
        rabbitTemplate.setReturnsCallback(this)
    }

    @RabbitListener(queues = ["confirm.queue"])
    fun receiveD(msg: Message, channel: Channel) {
        val data = msg.body.toString(Charset.defaultCharset())
        logger.info("队列收到消息，消息内容：{}", data)
    }

    @RabbitListener(queues = ["backup.queue.warn"])
    fun receiveWarn(msg: Message, channel: Channel) {
        val data = msg.body.toString(Charset.defaultCharset())
        logger.warn("报警队列收到消息，消息内容：{}", data)
    }

    // 交换机确认回调方法
    override fun confirm(correlationData: CorrelationData?, ack: Boolean, cause: String?) {
        if (ack && correlationData != null) {
            logger.info("交换机发送确认成功！id：{}，", correlationData.id)
        } else {
            logger.info("交换机发送确认失败！原因：{}，", cause)
        }
    }

    /**
     * 消息回退方法（消息不可达目的地时）
     */
    override fun returnedMessage(returned: ReturnedMessage) {
        logger.error(
            "消息{} 被交换机{}退回，原因：{}，路由key：{}。",
            returned.message.body.toString(Charset.defaultCharset()),
            returned.exchange,
            returned.replyText,
            returned.routingKey
        )
    }
}

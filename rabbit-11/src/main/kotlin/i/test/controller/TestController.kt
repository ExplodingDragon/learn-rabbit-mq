package i.test.controller

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Date

@RestController
@RequestMapping("/ttl")
class TestController(
    private val rabbitTemplate: RabbitTemplate
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/sendMsg/{message}")
    fun send(@PathVariable message: String) {
        logger.info("send message to TTL queue, date:{} ,message:{}", Date(), message)
        rabbitTemplate.convertAndSend("X", "XA", "XA队列消息: $message".toByteArray())
        rabbitTemplate.convertAndSend("X", "XB", "XB队列消息: $message".toByteArray())
    }

    @GetMapping("/sendMsg/{message}/{ttl}")
    fun sendTtl(@PathVariable message: String, @PathVariable ttl: String) {
        logger.info("send message to TTL queue, date:{} ,message:{},timeout: {} ms.", Date(), message, ttl)
        rabbitTemplate.convertAndSend(
            "X",
            "XC",
            "XC队列消息: $message".toByteArray()
        ) {
            it.messageProperties.expiration = ttl
            it
        }
    }
}

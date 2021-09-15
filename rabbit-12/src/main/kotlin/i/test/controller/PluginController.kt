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
class PluginController(
    private val rabbitTemplate: RabbitTemplate
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/send/{message}/{ttl}")
    fun sendTtl(@PathVariable message: String, @PathVariable ttl: Int) {
        logger.info("send message to TTL queue, date:{} ,message:{},timeout: {} ms.", Date(), message, ttl)
        rabbitTemplate.convertAndSend(
            "delayed.exchange",
            "delayed.routing.key",
            "发送消息: $message".toByteArray()
        ) {
            it.messageProperties.delay = ttl
            it
        }
    }
}

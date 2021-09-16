package i.test.controller

import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/confirm")
class ConfirmController(
    private val rabbitTemplate: RabbitTemplate
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/send/{message}")
    fun sendTtl(@PathVariable message: String) {
        logger.info("发送消息：{}", message)
        val correlationData = CorrelationData("1")
        rabbitTemplate.convertAndSend(
            "confirm.exchange",
            "confirm.routing.key",
            "发送消息: $message".toByteArray(),
            correlationData
        )
    }
    @GetMapping("/fail/exchange/{message}")
    fun failExchange(@PathVariable message: String) {
        logger.info("发送消息：{}", message)
        val correlationData = CorrelationData("1")
        rabbitTemplate.convertAndSend(
            "confirm.exchange.fail",
            "confirm.routing.key",
            "发送消息: $message".toByteArray(),
            correlationData
        )
    }
    @GetMapping("/fail/route/{message}")
    fun failRoute(@PathVariable message: String) {
        logger.info("发送消息：{}", message)
        val correlationData = CorrelationData("1")
        rabbitTemplate.convertAndSend(
            "confirm.exchange",
            "confirm.routing.fail",
            "发送消息: $message".toByteArray(),
            correlationData
        )
    }
}

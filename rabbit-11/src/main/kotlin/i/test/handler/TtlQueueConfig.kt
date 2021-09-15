package i.test.handler

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TtlQueueConfig {
    @Bean
    fun xExchange() = DirectExchange("X")

    @Bean
    fun yExchange() = DirectExchange("Y")

    @Bean
    fun queueA(): Queue = QueueBuilder.durable("QA")
        .withArguments(
            mapOf(
                Pair("x-dead-letter-exchange", "Y"),
                Pair("x-message-ttl", 10_000),
                Pair("x-dead-letter-routing-key", "YD")
            )
        ).build()

    @Bean
    fun queueB(): Queue = QueueBuilder.durable("QB")
        .withArguments(
            mapOf(
                Pair("x-dead-letter-exchange", "Y"),
                Pair("x-message-ttl", 40_000),
                Pair("x-dead-letter-routing-key", "YD")
            )
        ).build()

    @Bean
    fun queueC(): Queue = QueueBuilder.durable("QC")
        .withArguments(
            mapOf(
                Pair("x-dead-letter-exchange", "Y"),
                Pair("x-dead-letter-routing-key", "YD")
            )
        ).build()

    @Bean
    fun queueD(): Queue = QueueBuilder.durable("QD").build()

    @Bean
    fun queueABindingX(
        @Qualifier("queueA") queue: Queue,
        @Qualifier("xExchange") exchange: DirectExchange
    ): Binding = BindingBuilder.bind(queue).to(exchange).with("XA")

    @Bean
    fun queueBBindingX(
        @Qualifier("queueB") queue: Queue,
        @Qualifier("xExchange") exchange: DirectExchange
    ): Binding = BindingBuilder.bind(queue).to(exchange).with("XB")

    @Bean
    fun queueCBindingX(
        @Qualifier("queueC") queue: Queue,
        @Qualifier("xExchange") exchange: DirectExchange
    ): Binding = BindingBuilder.bind(queue).to(exchange).with("XC")

    @Bean
    fun queueDBindingY(
        @Qualifier("queueD") queue: Queue,
        @Qualifier("yExchange") exchange: DirectExchange
    ): Binding = BindingBuilder.bind(queue).to(exchange).with("YD")
}

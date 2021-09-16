package i.test.handler

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfirmQueueConfig {
    @Bean
    fun queue(): Queue = QueueBuilder.durable("priority.queue").build()

    @Bean
    fun exchange(): DirectExchange = ExchangeBuilder.directExchange("priority.exchange")
        .withArgument("x-max-priority", 255).build()

    @Bean
    fun binding(
        @Qualifier("queue") queue: Queue,
        @Qualifier("exchange") exchange: DirectExchange
    ): Binding = BindingBuilder
        .bind(queue).to(exchange).with("priority.routing.key")
}

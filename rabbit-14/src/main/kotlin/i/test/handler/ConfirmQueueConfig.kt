package i.test.handler

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfirmQueueConfig {
    @Bean
    fun queue(): Queue = QueueBuilder.durable("confirm.queue").build()

    /**
     * 备份队列
     */
    @Bean
    fun backupExchange() = FanoutExchange("backup.exchange")

    @Bean
    fun backupQueue(): Queue = QueueBuilder.durable("backup.queue.backup").build()

    @Bean
    fun warningQueue(): Queue = QueueBuilder.durable("backup.queue.warn").build()

    @Bean
    fun exchange(): DirectExchange = ExchangeBuilder
        .directExchange("confirm.exchange")
        .durable(true)
        .withArguments(
            mapOf(
                "alternate-exchange" to "backup.exchange"
            )
        ).build()

    @Bean
    fun binding(
        @Qualifier("queue") queue: Queue,
        @Qualifier("exchange") exchange: DirectExchange
    ): Binding = BindingBuilder
        .bind(queue).to(exchange).with("confirm.routing.key")

    @Bean
    fun backupBinding(
        @Qualifier("backupQueue") queue: Queue,
        @Qualifier("backupExchange") exchange: FanoutExchange
    ): Binding = BindingBuilder
        .bind(queue).to(exchange)

    @Bean
    fun backupWarnBinding(
        @Qualifier("warningQueue") queue: Queue,
        @Qualifier("backupExchange") exchange: FanoutExchange
    ): Binding = BindingBuilder
        .bind(queue).to(exchange)
}

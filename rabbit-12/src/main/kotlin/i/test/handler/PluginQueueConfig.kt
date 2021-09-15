package i.test.handler

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.CustomExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PluginQueueConfig {
    @Bean
    fun queue() = Queue("delayed.queue")

    @Bean
    fun exchange() = kotlin.run {
        CustomExchange(
            "delayed.exchange",
            "x-delayed-message",
            true,
            false,
            mapOf(
                "x-delayed-type" to "direct"
            )
        )
    }

    @Bean
    fun binding(
        @Qualifier("queue") queue: Queue,
        @Qualifier("exchange") exchange: CustomExchange
    ): Binding = BindingBuilder
        .bind(queue).to(exchange).with("delayed.routing.key").noargs()
}

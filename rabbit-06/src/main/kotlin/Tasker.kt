import com.rabbitmq.client.MessageProperties
import java.util.concurrent.ConcurrentSkipListMap

/**
 * 持久化队列
 *
 * 开启发布确认
 */
object Tasker {
    private const val QUEUE_NAME = "confirm_queue"

    @JvmStatic
    fun main(args: Array<String>) {
        confirmAsync()
//        confirmOne()
//        confirmMany()
    }

    private fun confirmAsync() {
        val channel = RabbitMQUtils.createChannel()
        channel.confirmSelect()
        channel.queueDeclare(QUEUE_NAME, true, false, false, null)
        // 添加异步监听消息确认
        val acceptConfirms = ConcurrentSkipListMap<Long, String>()
        channel.addConfirmListener(
            { arg1, arg2 ->
                if (arg2) {
                    acceptConfirms.headMap(arg1)
                    println("多个确认成功：$arg1")
                } else {
                    // 成功确认
                    acceptConfirms.remove(arg1)
                    println("单个确认成功：$arg1")
                }
            },
            { arg1, arg2 ->
                // 失败确认
                println("确认失败：$arg1")
            }
        )
        for (i in 0..100) {
            val s = "message count:$i"
            acceptConfirms[channel.nextPublishSeqNo] = s
            channel
                .basicPublish(
                    "", QUEUE_NAME,
                    // 消息持久化
                    null, s.toByteArray()
                )
        }
    }

    private fun confirmMany() {
        val channel = RabbitMQUtils.createChannel()
        channel.confirmSelect()
        channel.queueDeclare(QUEUE_NAME, true, false, false, null)
        for (i in 0..100) {
            channel
                .basicPublish(
                    "", QUEUE_NAME,
                    // 消息持久化
                    MessageProperties.PERSISTENT_TEXT_PLAIN, "message count:$i".toByteArray()
                )
        }
        channel.waitForConfirms()
    }

    private fun confirmOne() {
        val channel = RabbitMQUtils.createChannel()
        channel.confirmSelect()
        channel.queueDeclare(QUEUE_NAME, true, false, false, null)
        for (i in 0..100) {
            channel
                .basicPublish(
                    "", QUEUE_NAME,
                    // 消息持久化
                    MessageProperties.PERSISTENT_TEXT_PLAIN, "message count:$i".toByteArray()
                )
            channel.waitForConfirms()
        }
    }
}

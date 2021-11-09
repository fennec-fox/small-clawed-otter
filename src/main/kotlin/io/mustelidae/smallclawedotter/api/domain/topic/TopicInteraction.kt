package io.mustelidae.smallclawedotter.api.domain.topic

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.domain.topic.repository.TopicRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TopicInteraction(
    private val topicRepository: TopicRepository,
    private val topicFinder: TopicFinder
) {

    fun add(
        productCode: ProductCode,
        code: ObjectId,
        name: String,
        description: String? = null
    ): Topic {
        val topic = Topic(
            productCode,
            code.toString(),
            name,
            description
        )
        return topicRepository.save(topic)
    }

    fun expire(
        code: String
    ) {
        val topic = topicFinder.findOne(code)
        topic.expire()
        topicRepository.save(topic)
    }

    fun modify(
        code: String,
        name: String,
        description: String? = null
    ) {
        val topic = topicFinder.findOne(code)
        topic.apply {
            this.name = name
            this.description = description
        }

        topicRepository.save(topic)
    }
}

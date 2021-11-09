package io.mustelidae.smallclawedotter.api.domain.topic.api

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import org.bson.types.ObjectId

class TopicResources {

    data class Request(
        val code: String = ObjectId().toString(),
        val name: String,
        val description: String? = null,
    )

    data class Modify(
        val name: String,
        val description: String? = null
    )

    data class Response(
        val id: Long,
        val code: String,
        val name: String,
        val description: String? = null
    ) {
        companion object {
            fun from(topic: Topic): Response {
                return topic.run {
                    Response(id!!, code, name, description)
                }
            }
        }
    }
}

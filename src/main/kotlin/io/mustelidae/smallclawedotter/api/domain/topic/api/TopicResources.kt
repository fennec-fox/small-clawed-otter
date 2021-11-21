package io.mustelidae.smallclawedotter.api.domain.topic.api

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import io.swagger.annotations.ApiModel
import org.bson.types.ObjectId

class TopicResources {

    @ApiModel("Topic.Request")
    data class Request(
        val code: String = ObjectId().toString(),
        val name: String,
        val description: String? = null,
    )

    @ApiModel("Topic.Modify")
    data class Modify(
        val name: String,
        val description: String? = null
    )

    @ApiModel("Topic.Reply")
    data class Reply(
        val id: Long,
        val code: String,
        val name: String,
        val description: String? = null
    ) {
        companion object {
            fun from(topic: Topic): Reply {
                return topic.run {
                    Reply(id!!, code, name, description)
                }
            }
        }
    }
}

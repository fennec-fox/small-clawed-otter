package io.mustelidae.smallclawedotter.api.domain.topic

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.utils.invokeId
import org.bson.types.ObjectId

internal class TopicTest

internal fun Topic.Companion.aFixture(hasId: Boolean = false): Topic {
    return Topic(
        ProductCode.NOTICE,
        ObjectId().toString(),
        "this is test topic",
    ).apply {
        invokeId(this, hasId)
    }
}

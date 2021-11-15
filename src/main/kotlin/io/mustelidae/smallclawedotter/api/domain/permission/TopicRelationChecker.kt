package io.mustelidae.smallclawedotter.api.domain.permission

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.domain.board.Writing

class TopicRelationChecker(
    private val productCode: ProductCode,
    private val topicCode: String
) : AuthChecker {
    override fun checkWriting(writing: Writing): Boolean {
        return (writing.topic!!.code == topicCode && writing.topic!!.productCode == productCode)
    }

    override fun checkWritings(writings: List<Writing>): Boolean {
        var isOk = true
        for (writing in writings)
            isOk = checkWriting(writing) && isOk

        return isOk
    }
}

package io.mustelidae.smallclawedotter.api.domain.permission

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.config.PermissionException
import io.mustelidae.smallclawedotter.api.domain.board.Writing
import io.mustelidae.smallclawedotter.api.domain.topic.Topic

class TopicRelationChecker(
    productCode: ProductCode,
    private val topic: Topic
) : AuthChecker {
    override fun checkWriting(writing: Writing): Boolean {
        return (writing.topic!!.id!! == topic.id!!)
    }

    override fun checkWritings(writings: List<Writing>): Boolean {
        var isOk = true
        for (writing in writings)
            isOk = checkWriting(writing) && isOk

        return isOk
    }

    init {
        if (topic.productCode != productCode)
            throw PermissionException("productCode에 대한 Topic의 권한이 없습니다.")
    }
}

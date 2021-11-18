package io.mustelidae.smallclawedotter.api.domain.permission

import io.mustelidae.smallclawedotter.api.domain.board.Writing

interface AuthChecker {

    fun checkWriting(writing: Writing): Boolean
    fun checkWritings(writings: List<Writing>): Boolean
}

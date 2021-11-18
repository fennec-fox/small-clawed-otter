package io.mustelidae.smallclawedotter.utils

import com.fasterxml.jackson.module.kotlin.readValue
import io.mustelidae.smallclawedotter.api.utils.Jackson
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.ContentResultMatchersDsl
import java.nio.charset.StandardCharsets

internal inline fun <reified T> ContentResultMatchersDsl.target(crossinline matcher: (obj: T) -> Unit): ResultMatcher = ResultMatcher {
    matcher(
        Jackson.getMapper().readValue(it.response.getContentAsString(StandardCharsets.UTF_8))
    )
}

package io.mustelidae.smallclawedotter.api.common

interface ErrorSource {
    val code: String
    val message: String
    var causeBy: Map<String, Any?>?
    var refCode: String?
}

package io.mustelidae.smallclawedotter.api.common

enum class ErrorCode(val summary: String) {

    H000("Human error"),

    HD00("Data not found"),
    HD01("No results found"),

    HA00("Unauthorized"),

    HI00("Invalid Input"),
    HI01("Invalid argument"),
    HI02("Invalid header argument"),

    P000("policy error"),
    PD01("develop mistake error"),

    S000("common system error"),
    SA00("async execute error"),
    SI01("illegal state error"),
    SD01("database access error"),

    C000("communication error"),
    CT01("connection timeout"),
    CT02("read timeout"),
}

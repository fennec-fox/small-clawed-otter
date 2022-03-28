package io.mustelidae.smallclawedotter.api.config

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "Common Error format")
data class GlobalErrorFormat(
    val timestamp: String,
    @ApiModelProperty(value = "Http Status Code")
    val status: Int,
    @ApiModelProperty(value = "error code")
    val code: String,
    @ApiModelProperty(value = "exception name")
    val type: String,
    @ApiModelProperty(value = "exception message")
    val message: String,
    @ApiModelProperty(value = "text displayed to the user")
    val description: String? = null,
    @ApiModelProperty(value = "reference error code")
    val refCode: String? = null
)

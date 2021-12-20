package io.mustelidae.smallclawedotter.api.config

import com.fasterxml.classmate.TypeResolver
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger.web.UiConfigurationBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Configuration
class SwaggerConfiguration {

    @Autowired
    private lateinit var typeResolver: TypeResolver

    @Bean
    fun default(): Docket = Docket(DocumentationType.SWAGGER_2)
        .directModelSubstitute(LocalDate::class.java, String::class.java)
        .directModelSubstitute(LocalDateTime::class.java, String::class.java)
        .directModelSubstitute(LocalTime::class.java, String::class.java)
        .groupName("API")
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.mustelidae.smallclawedotter.api.domain"))
        .paths(PathSelectors.regex("(?!/maintenance).+"))
        .build()
        .additionalModels(typeResolver.resolve(GlobalErrorFormat::class.java))

    @Bean
    fun maintenance(): Docket = Docket(DocumentationType.SWAGGER_2)
        .directModelSubstitute(LocalDate::class.java, String::class.java)
        .directModelSubstitute(LocalDateTime::class.java, String::class.java)
        .directModelSubstitute(LocalTime::class.java, String::class.java)
        .groupName("Maintenance")
        .select()
        .apis(RequestHandlerSelectors.basePackage("io.mustelidae.smallclawedotter.api.domain"))
        .paths(PathSelectors.ant("/maintenance/**"))
        .build()
        .additionalModels(typeResolver.resolve(GlobalErrorFormat::class.java))

    @Profile("prod")
    @Bean
    fun uiConfig(): UiConfiguration = UiConfigurationBuilder.builder()
        .supportedSubmitMethods(UiConfiguration.Constants.NO_SUBMIT_METHODS)
        .build()
}

@ApiModel(description = "Common Error format")
data class GlobalErrorFormat(
    val timestamp: String,
    @ApiModelProperty(value = "Http Status Code")
    val status: Int,
    @ApiModelProperty(value = "error code")
    val code: String,
    @ApiModelProperty(value = "text displayed to the user")
    val description: String,
    @ApiModelProperty(value = "exception message")
    val message: String,
    @ApiModelProperty(value = "exception name")
    val type: String,
    @ApiModelProperty(value = "reference error code")
    val refCode: String
)

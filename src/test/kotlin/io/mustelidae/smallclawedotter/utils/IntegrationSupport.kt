package io.mustelidae.smallclawedotter.utils

import io.mustelidae.smallclawedotter.SmallClawedOtterApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@ActiveProfiles("embedded")
@SpringBootTest(classes = [SmallClawedOtterApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationSupport {
    @LocalServerPort
    var port: Int = 0
    var address: String = "http://localhost"

    @Autowired
    lateinit var mockMvc: MockMvc
}

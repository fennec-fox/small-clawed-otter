import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jmailen.kotlinter") version "3.6.0"
    id("com.avast.gradle.docker-compose") version "0.14.9"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
    kotlin("plugin.allopen") version "1.5.31"
    kotlin("plugin.noarg") version "1.5.31"
    kotlin("kapt") version "1.5.31"
}

group = "io.mustelidae"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenLocal()
    mavenCentral()
}

ext["log4j2.version"] = "2.17.0"

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-envers")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.mongodb:bson:4.4.0")
    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas")
    testImplementation("org.reflections:reflections:0.10.2")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")

    testImplementation("com.h2database:h2")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    implementation("com.querydsl:querydsl-core:5.0.0")
    implementation("com.querydsl:querydsl-jpa:5.0.0")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.3")
    implementation("io.springfox:springfox-boot-starter:3.0.0")


}

noArg {
    invokeInitializers = true
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) {
                val output =
                    "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
                val startItem = "|  "
                val endItem = "  |"
                val repeatLength = startItem.length + output.length + endItem.length
                println("\n${"-".repeat(repeatLength)}\n|  $output  |\n${"-".repeat(repeatLength)}")
            }
        }
    })

    // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html
    // test jvm 에 대한 힙사이즈 할당
    minHeapSize = "1024m"
    maxHeapSize = "4096m" // 최대 힙사이즈
}

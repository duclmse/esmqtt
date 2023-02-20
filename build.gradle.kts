

group = "com.iot"
version = "1.0-SNAPSHOT"

plugins {
    java
    idea
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.google.com/") }
}

val versions = mapOf("boot" to "3.0.0")

fun spring(project: String, module: String = ""): String {
    return "org.springframework.$project:spring-$project$module:${versions[project]}"
}

dependencies {
    "org.projectlombok:lombok:1.18.26".let {
        compileOnly(it)
        annotationProcessor(it)
        testCompileOnly(it)
        testAnnotationProcessor(it)
    }

    compileOnly(spring("boot", "-configuration-processor"))
    annotationProcessor(spring("boot", "-configuration-processor"))

    implementation(spring("boot", "-starter-jdbc"))
    implementation(spring("boot", "-starter-webflux"))
    implementation(spring("boot", "-starter-security"))
    implementation("org.springframework.integration:spring-integration-mqtt:6.0.2")

    implementation("com.mysql:mysql-connector-j:8.0.32")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

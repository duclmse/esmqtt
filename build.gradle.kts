import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "com.iot"
version = "1.0-SNAPSHOT"

plugins {
    java
    idea
    id("org.springframework.boot") version "2.7.9"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.google.com/") }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.getByName<BootJar>("bootJar") {
    manifest {
        attributes("Start-Class" to "com.iot.Application")
    }
}

val versions = mapOf("boot" to "2.7.9")

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
    implementation(spring("boot", "-starter-actuator"))
    implementation(spring("boot", "-starter-validation"))

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.2")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("com.mysql:mysql-connector-j:8.0.32")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

plugins {
    id("java")
    id ("checkstyle")
    application
    jacoco
    id("io.freefair.lombok") version "8.10.2"
    id ("com.adarshr.test-logger") version "3.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("App")
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation(platform("org.junit:junit-bom:5.11.0-M1"))

    implementation("io.javalin:javalin:6.1.3")
    implementation("gg.jte:jte:3.1.9")
    implementation("io.javalin:javalin-rendering:6.1.3")
    implementation("org.slf4j:slf4j-simple:2.0.7")

    implementation("com.h2database:h2:2.2.220")
    implementation("com.zaxxer:HikariCP:6.0.0")
}

tasks.test {
    useJUnitPlatform()
}


tasks.shadowJar{
    destinationDirectory.set(file(System.getProperty("user.dir")))
}


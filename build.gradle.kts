plugins {
    id("java")
    application
    id("org.openjfx.javafxplugin") version("0.1.0")
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}


javafx {
    version = "17.0.10" // Highly stable version that fully supports Java 11
    modules = listOf("javafx.controls") // Pulls in critical UI components like Button, Scene, and Stage
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ui.Main") // 👈 Points Gradle to your main class
}

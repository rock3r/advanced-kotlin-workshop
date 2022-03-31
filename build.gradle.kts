import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
}

group = "dev.sebastiano.workshop"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("scripting-jsr223"))

    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("org.jsoup:jsoup:1.14.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Test> {
        useJUnitPlatform()
    }
}

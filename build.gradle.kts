plugins {
    alias(libs.plugins.kotlinJvm)
}

group = "dev.sebastiano.workshop"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.kotlin)

    testImplementation(libs.assertk)
    testImplementation(libs.jsoup)
    testImplementation(libs.junit.jupiter)
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

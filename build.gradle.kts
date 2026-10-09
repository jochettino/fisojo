import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
    application
}

group = "com.github.jochettino"
version = "1.3-SNAPSHOT"
description = "Fisojo, the ugliest CR notifier for Slack"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect", "1.3.11"))
    implementation("com.google.code.gson:gson:2.8.2")
    implementation("org.apache.httpcomponents:httpclient:4.5.3")
    implementation("org.apache.logging.log4j:log4j-api:2.11.2")
    implementation("org.apache.logging.log4j:log4j-core:2.13.2")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.12")
}

application {
    mainClassName = "com.github.jochettino.fisojo.RunKt"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// Self-contained jar (replaces maven-assembly-plugin's jar-with-dependencies)
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("jar-with-dependencies")
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    from(sourceSets.main.get().output)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) }
    })
}

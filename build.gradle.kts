plugins {
    kotlin("jvm") version "2.4.21"
    application
}

group = "com.github.jochettino"
version = "1.3-SNAPSHOT"
description = "Fisojo, the ugliest CR notifier for Slack"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.14.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("org.apache.logging.log4j:log4j-api:2.26.1")
    implementation("org.apache.logging.log4j:log4j-core:2.26.1")

    testImplementation(kotlin("test-junit"))
    testImplementation("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(26)
}

application {
    mainClass.set("com.github.jochettino.fisojo.RunKt")
}

// Self-contained jar (replaces maven-assembly-plugin's jar-with-dependencies)
tasks.register<Jar>("fatJar") {
    archiveClassifier.set("jar-with-dependencies")
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    from(sourceSets.main.get().output)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.map { zipTree(it) }
    })
}

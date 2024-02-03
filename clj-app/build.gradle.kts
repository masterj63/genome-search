plugins {
    id("dev.clojurephant.clojure") version "0.8.0-beta.7"
    id("application")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

repositories {
    maven(url = "https://repo.clojars.org") {
        name = "Clojars"
    }
    mavenCentral()
}

dependencies {
    implementation("org.clojure:clojure:1.11.1")
    implementation("com.google.guava:guava:33.0.0-jre")
    testRuntimeOnly("dev.clojurephant:jovial:0.4.1")
    devImplementation("org.clojure:tools.namespace:1.3.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

clojure {
    builds {
        getByName("main") {
            aotAll()
        }
    }
}

application {
    mainClass = "main.main"
}
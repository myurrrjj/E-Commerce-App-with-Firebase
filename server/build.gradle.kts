val ktor_version = "2.3.7"
val djl_version = "0.26.0"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.0.21"
    id("application")
}

group = "com.example.koshi"
version = "0.0.1"

application {
    mainClass.set("com.example.koshi.ApplicationKt")
}

dependencies {
    // 1. Ktor Server
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    // 2. Data Handling (JSON)
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:1.4.14")

    // 3. AI/ML (DJL)

        // ... your existing ktor dependencies ...

        // DJL Core (You likely already have these)
    implementation(platform("ai.djl:bom:0.29.0")) // Using a slightly newer, stable version

    // 2. Core DJL APIs (No version numbers needed anymore!)
    implementation("ai.djl:api")
    implementation("ai.djl:model-zoo")

    // 3. The Engine & Native Code
    runtimeOnly("ai.djl.pytorch:pytorch-engine")
    runtimeOnly("ai.djl.pytorch:pytorch-model-zoo")

    // Switch 'auto' to 'cpu' to be safe and compatible with standard laptops
    runtimeOnly("ai.djl.pytorch:pytorch-native-cpu")


    // CHANGED: We removed "auto" and FORCED the Windows 64-bit version
    // This bypasses the detection error completely.
//    runtimeOnly("ai.djl.pytorch:pytorch-native-cpu:$djl_version:win-x86_64")
}
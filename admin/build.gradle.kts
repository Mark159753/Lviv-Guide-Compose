import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.apollographql.apollo3") version "3.8.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven {
        url = uri("https://repo.repsy.io/mvn/chrynan/public")
    }
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.apollographql.apollo3:apollo-runtime:3.8.2")

                //Koin
                val koin = "3.2.0"
                implementation("io.insert-koin:koin-core:${koin}")
                implementation("io.insert-koin:koin-test:${koin}")
                implementation("io.insert-koin:koin-compose:1.0.3")
            }
        }
        val jvmTest by getting
    }
}

apollo {
    service("service") {
        srcDir("src/graphql")
        mapScalarToUpload("Upload")
        generateKotlinModels.set(true)
        packageName.set("com.example")
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "lvivGuideAdmin"
            packageVersion = "1.0.0"
        }
    }
}

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.gaoyang"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.alibaba:easyexcel:3.3.2")
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
    implementation("org.openjfx:javafx-swing:16")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Excel-Search"
            packageVersion = "1.0.0"

            macOS {
                dockName = "Excel搜索助手"
                iconFile.set(project.file("icons/logo.icns"))
            }
            linux {
                iconFile.set(project.file("icons/logo.png"))
            }
            windows {
                shortcut = true
                dirChooser = true
                iconFile.set(project.file("icons/logo.ico"))
            }
        }
    }
}
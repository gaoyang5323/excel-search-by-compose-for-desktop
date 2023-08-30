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
    implementation("org.apache.poi:poi:5.2.3")
    implementation("com.alibaba:easyexcel:3.3.2"){
        exclude("org.apache.poi:poi-ooxml-schemas")
    }
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
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
            packageVersion = "2.0.1"

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
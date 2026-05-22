import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "MaterialColorUtils"
            isStatic = true
        }
    }

    js { browser() }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }

    androidLibrary {
        namespace = "com.google.materialcolorutils"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    sourceSets {
        commonMain {
            kotlin {
                // List source subdirectories explicitly instead of "." to avoid a Gradle
                // task-ordering conflict: wasmJsPublicPackageJson writes package.json to
                // the project root, and srcDirs(".") would make the compiler read that same
                // root — triggering an implicit-dependency warning.
                srcDirs(
                    "blend",
                    "contrast",
                    "dislike",
                    "dynamiccolor",
                    "hct",
                    "palettes",
                    "scheme",
                    "temperature",
                    "utils",
                )
                // StringUtils uses java.text / java.util.Locale — not KMP-compatible
                exclude("StringUtils.kt")
            }
        }

        // @JvmStatic is a JVM-only annotation. Provide a no-op stub on non-JVM targets
        // so the library compiles without modification. The JVM/Android target picks up
        // the real annotation from the Kotlin stdlib; these targets get the stub instead.
        val jvmStubs = "kmpStubs"
        iosMain   { kotlin.srcDir(jvmStubs) }
        jsMain    { kotlin.srcDir(jvmStubs) }
        wasmJsMain { kotlin.srcDir(jvmStubs) }
    }
}

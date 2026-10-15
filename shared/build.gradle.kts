import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins { kotlin("multiplatform") }
kotlin {
    jvm()
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser() }
    sourceSets {
        commonTest.dependencies { implementation(kotlin("test")) }
    }
    jvmToolchain(17)
}

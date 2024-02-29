
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        additionalEditorconfig.set(
            mapOf(
                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
            ),
        )
    }

    dependencies {
        ktlintRuleset("io.nlopez.compose.rules:ktlint:0.3.11")
    }
}

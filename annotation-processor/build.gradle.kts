import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (annotation processor)") {
	targets {
		jvm {
			@Suppress("SpellCheckingInspection")
			dependencies {
				implementation(project(":fluid-json-annotations"))
				implementation(project(":fluid-json-coding"))
				implementation(kotlin("reflect"))
				implementation(fluid("meta", "0.15.0"))
				implementation("com.google.auto:auto-common:1.2.2")
				implementation("com.google.auto.service:auto-service:1.1.1")
				implementation("com.squareup:kotlinpoet:2.2.0")

				kapt("com.google.auto.service:auto-service:1.1.1")
			}

			testDependencies {
				implementation(fluid("compiler", "0.15.0"))
			}
		}
	}
}

tasks.named("dokkaHtml") {
	dependsOn("kaptKotlinJvm")
}

tasks.named<Test>("jvmTest") {
	// KAPT accesses JDK compiler internals via reflection.
	val javacPackages = listOf("api", "code", "comp", "file", "jvm", "main", "model", "parser", "processing", "tree", "util")
	jvmArgs(javacPackages.flatMap { pkg ->
		listOf(
			"--add-exports", "jdk.compiler/com.sun.tools.javac.$pkg=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.$pkg=ALL-UNNAMED",
		)
	})
}

kotlin {
	jvmToolchain(21)
}

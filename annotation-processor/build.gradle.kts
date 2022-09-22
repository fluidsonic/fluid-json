import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (annotation processor)") {
	targets {
		jvm {
			withJava()

			@Suppress("SpellCheckingInspection")
			dependencies {
				implementation(project(":fluid-json-annotations"))
				implementation(project(":fluid-json-coding"))
				implementation(kotlin("reflect"))
				implementation(fluid("meta", "0.13.0"))
				implementation("com.google.auto:auto-common:1.2.1")
				implementation("com.google.auto.service:auto-service:1.0.1")
				implementation("com.squareup:kotlinpoet:1.12.0")

				kapt("com.google.auto.service:auto-service:1.0.1")
			}

			testDependencies {
				implementation(fluid("compiler", "0.12.0"))
			}
		}
	}
}

// https://youtrack.jetbrains.com/issue/KT-45545
if (JavaVersion.current().isJava9Compatible)
	tasks.withType<Test>().all {
		jvmArgs(
			"--add-opens", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
			"--add-opens", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
		)
	}

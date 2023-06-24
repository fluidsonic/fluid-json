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
				implementation(fluid("meta", "0.14.0"))
				implementation("com.google.auto:auto-common:1.2.2")
				implementation("com.google.auto.service:auto-service:1.1.1")
				implementation("com.squareup:kotlinpoet:1.14.2")

				kapt("com.google.auto.service:auto-service:1.1.1")
			}

			testDependencies {
				implementation(fluid("compiler", "0.13.0"))
			}
		}
	}
}

tasks.named("dokkaHtml") {
	dependsOn("kaptKotlinJvm")
}

kotlin {
	jvmToolchain(8)
}

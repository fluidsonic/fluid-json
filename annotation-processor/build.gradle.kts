import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (annotation processor)") {
	targets {
		jvm {
			withJava()

			dependencies {
				implementation(project(":fluid-json-annotations"))
				implementation(project(":fluid-json-coding"))
				implementation(kotlin("reflect"))
				implementation(fluid("meta", "0.11.1-kotlin-1.5"))
				implementation("com.google.auto:auto-common:0.11")
				implementation("com.google.auto.service:auto-service:1.0-rc7")
				implementation("com.squareup:kotlinpoet:1.7.2")

				kapt("com.google.auto.service:auto-service:1.0-rc7")
			}

			testDependencies {
				implementation(fluid("compiler", "0.10.3-kotlin-1.5"))
			}
		}
	}
}

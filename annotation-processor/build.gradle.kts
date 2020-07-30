import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (annotation processor)") {
	targets {
		jvm {
			withJava()

			dependencies {
				implementation(project(":fluid-json-annotations"))
				implementation(project(":fluid-json-coding"))
				implementation(kotlin("reflect"))
				implementation(fluid("meta", "0.10.1"))
				implementation(fluid("stdlib", "0.10.0"))
				implementation("com.google.auto:auto-common:0.10")
				implementation("com.google.auto.service:auto-service:1.0-rc6")
				implementation("com.squareup:kotlinpoet:1.5.0")

				kapt("com.google.auto.service:auto-service:1.0-rc6")
			}

			testDependencies {
				implementation(fluid("compiler", "0.10.1"))
			}
		}
	}
}

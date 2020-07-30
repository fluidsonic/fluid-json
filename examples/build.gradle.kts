import io.fluidsonic.gradle.*

fluidLibraryModule(description = "examples") {
	withoutPublishing()

	language {
		withoutExplicitApi()
	}

	targets {
		jvm {
			withJava()

			dependencies {
				implementation(project(":fluid-json-coding-jdk8"))
				implementation(project(":fluid-json-annotations"))

				kapt(project(":fluid-json-annotation-processor"))
			}
		}
	}
}

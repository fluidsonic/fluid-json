import io.fluidsonic.gradle.*

fluidLibraryModule(description = "examples") {
	withoutPublishing()

	language {
		withoutExplicitApi()
	}

	targets {
		jvmJdk8 {
			withJava()

			dependencies {
				implementation(project(":fluid-json-coding-jdk8"))
				implementation(project(":fluid-json-annotations"))

				kapt(project(":fluid-json-annotation-processor"))
			}
		}
	}
}

import io.fluidsonic.gradle.*

fluidLibraryModule(description = "examples") {
	withoutPublishing()

	language {
		withoutExplicitApi()
	}

	targets {
		jvm {
			dependencies {
				implementation(project(":fluid-json-coding"))
				implementation(project(":fluid-json-annotations"))

				kapt(project(":fluid-json-annotation-processor"))
			}
		}
	}
}

kotlin {
	jvmToolchain(21)
}

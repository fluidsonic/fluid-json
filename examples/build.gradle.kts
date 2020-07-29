import io.fluidsonic.gradle.*

plugins {
	kotlin("multiplatform")
	kotlin("kapt")
}

fluidLibraryModule(description = "examples") {
	withoutPublishing()

	language {
		withoutExplicitApi()
	}

	targets {
		jvm {
			dependencies {
				implementation(project(":fluid-json-coding-jdk8"))
				implementation(project(":fluid-json-annotations"))
			}
		}
	}
}

dependencies {
	"kapt"(project(":fluid-json-annotation-processor"))
}

import io.fluidsonic.gradle.*

fluidLibraryModule(description = "A JSON library written in pure Kotlin (Ktor Client extension)") {
	targets {
		jvmJdk7 {
			dependencies {
				api(project(":fluid-json-coding"))
				api("io.ktor:ktor-client-json-jvm:1.3.2-1.4.0-rc")
			}
		}
	}
}

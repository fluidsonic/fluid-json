description = "A JSON library written in pure Kotlin (Ktor Client extension)"

configurePublishing()

dependencies {
	api(project(":fluid-json-coding"))

	api("io.ktor:ktor-client-json-jvm:1.1.2")
}

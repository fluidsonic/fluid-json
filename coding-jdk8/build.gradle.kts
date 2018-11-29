description = "A JSON library written in pure Kotlin (JDK8+ coding extension)"

configureJDK8()
configurePublishing()

dependencies {
	api(project(":fluid-json-coding"))
}

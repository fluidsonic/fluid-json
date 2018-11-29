description = "A JSON library written in pure Kotlin (JDK8+ coding extension)"

configureJDK8()
configurePublishing(id = "fluid-json-coding-jdk8")

dependencies {
	api(project(":coding"))
}

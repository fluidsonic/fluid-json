description = "A JSON library written in pure Kotlin (coding extension)"

configurePublishing(id = "fluid-json-coding")

dependencies {
	api(project(":basic"))

	api(kotlin("reflect"))
}

description = "A JSON library written in pure Kotlin (coding extension)"

configurePublishing()

dependencies {
	api(project(":fluid-json-basic"))

	api(kotlin("reflect"))
}

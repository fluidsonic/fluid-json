import com.github.fluidsonic.fluid.library.*

fluidLibraryVariant {
	description = "A JSON library written in pure Kotlin (coding extension)"
}

dependencies {
	api(project(":fluid-json-basic"))

	api(kotlin("reflect"))
}

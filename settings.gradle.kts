rootProject.name = "fluid-json"

include("annotation-processor", "annotations", "basic", "coding", "examples", "ktor-serialization")

project(":annotation-processor").name = "fluid-json-annotation-processor"
project(":annotations").name = "fluid-json-annotations"
project(":basic").name = "fluid-json-basic"
project(":coding").name = "fluid-json-coding"
project(":examples").name = "fluid-json-examples"
project(":ktor-serialization").name = "fluid-json-ktor-serialization"

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		mavenLocal()
	}
}

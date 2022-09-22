rootProject.name = "fluid-json"

include("annotation-processor", "annotations", "basic", "coding", "coding-jdk8", "examples", "ktor-client", "ktor-serialization")

project(":annotation-processor").name = "fluid-json-annotation-processor"
project(":annotations").name = "fluid-json-annotations"
project(":basic").name = "fluid-json-basic"
project(":coding").name = "fluid-json-coding"
project(":coding-jdk8").name = "fluid-json-coding-jdk8"
project(":examples").name = "fluid-json-examples"
project(":ktor-client").name = "fluid-json-ktor-client"
project(":ktor-serialization").name = "fluid-json-ktor-serialization"

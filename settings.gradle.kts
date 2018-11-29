rootProject.name = "fluid-json"

include("basic", "coding", "coding-jdk8", "examples")

project(":basic").name = "fluid-json-basic"
project(":coding").name = "fluid-json-coding"
project(":coding-jdk8").name = "fluid-json-coding-jdk8"
project(":examples").name = "fluid-json-examples"

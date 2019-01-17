import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven


fun RepositoryHandler.bintray(name: String) =
	maven("https://dl.bintray.com/$name")

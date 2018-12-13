import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.*


fun DependencyHandler.kotlinx(module: String, version: String) =
	"org.jetbrains.kotlinx:kotlinx-$module:$version"


fun Project.configureKotlinx() {
	repositories {
		maven("https://kotlin.bintray.com/kotlinx")
	}
}

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


fun Project.configureJDK8() {
	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	tasks.withType<KotlinCompile> {
		sourceCompatibility = "1.8"
		targetCompatibility = "1.8"

		kotlinOptions.jvmTarget = "1.8"
	}

	dependencies {
		"api"(kotlin("stdlib-jdk8"))
	}
}

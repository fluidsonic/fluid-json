import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension


group = "com.github.fluidsonic"
version = "0.0.1"

plugins {
	kotlin("jvm")
	id("org.junit.platform.gradle.plugin") version "1.0.0"
}

configure<JavaPluginConvention> {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8

	sourceSets.getByName("main").apply {
		java.srcDirs("Sources")
		resources.srcDirs("Resources")
	}
	sourceSets.getByName("test").apply {
		java.srcDirs("Tests/Sources")
		resources.srcDirs("Tests/Resources")
	}
}

tasks.withType<JavaCompile> {
	sourceCompatibility = "1.8"
	targetCompatibility = "1.8"

	options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
	sourceCompatibility = "1.8"
	targetCompatibility = "1.8"

	kotlinOptions.jvmTarget = "1.8"
}

junitPlatform {
	platformVersion = "1.0.0"
	filters {
		engines {
			include("spek")
		}
	}
}


dependencies {
	compile(kotlin("stdlib"))
	compile("org.apiguardian:apiguardian-api:1.0.0")

	testCompile("org.jetbrains.spek:spek-api:1.1.5")
	testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
	testRuntime("org.junit.platform:junit-platform-runner:${junitPlatform.platformVersion}")
}

configurations.all {
	resolutionStrategy.apply {
		force("org.jetbrains.kotlin:kotlin-reflect:1.1.4-3")
		force("org.jetbrains.kotlin:kotlin-stdlib:1.1.4-3")

		failOnVersionConflict()
	}
}

repositories {
	jcenter()
	mavenCentral()
}


// extension for configuration
fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
	when (this) {
		is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
		else -> throw Exception("${this::class} must be an instance of ExtensionAware")
	}
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
	when (this) {
		is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
		else -> throw Exception("${this::class} must be an instance of ExtensionAware")
	}
}

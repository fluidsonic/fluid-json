import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


plugins {
	kotlin("jvm") version "1.3.20"
	`kotlin-dsl`
}

kotlinDslPluginOptions {
	experimentalWarning.set(false)
}

repositories {
	jcenter()
}

dependencies {
	implementation(kotlin("gradle-plugin"))
}

sourceSets {
	getByName("main") {
		kotlin.srcDirs("sources")
	}
}


val SourceSet.kotlin
	get() = withConvention(KotlinSourceSet::class) { kotlin }

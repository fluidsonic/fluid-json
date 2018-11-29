import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


plugins {
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

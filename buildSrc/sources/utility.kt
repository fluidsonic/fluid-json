import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


internal val NamedDomainObjectContainer<Configuration>.archives
	get() = named<Configuration>("archives")


internal fun Project.java(configuration: JavaPluginConvention.() -> Unit) =
	configure(configuration)


internal fun Project.publishing(configuration: PublishingExtension.() -> Unit) =
	configure(configuration)


internal val Project.signing
	get() = the<SigningExtension>()


internal fun Project.signing(configuration: SigningExtension.() -> Unit) =
	configure(configuration)


internal val Project.sourceSets
	get() = the<SourceSetContainer>()


val SourceSet.kotlin
	get() = withConvention(KotlinSourceSet::class) { kotlin }

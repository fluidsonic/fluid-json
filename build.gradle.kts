import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.bundling.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension


description = "A JSON library written in pure Kotlin."
group = "com.github.fluidsonic"
version = "0.9.1"


plugins {
	kotlin("jvm") version "1.1.60"
	jacoco
	`java-library`
	maven
	`maven-publish`
	signing
	id("com.github.ben-manes.versions") version "0.17.0"
	id("org.junit.platform.gradle.plugin") version "1.0.2"
}


java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8

	sourceSets {
		"examples" {
			kotlin.srcDirs("Examples")
		}

		"main" {
			kotlin.srcDirs("Sources")
		}

		"test" {
			kotlin.srcDirs("Tests/Sources")
			resources.srcDirs("Tests/Resources")
		}
	}
}

junitPlatform {
	platformVersion = "1.0.2"

	filters {
		engines {
			include("spek")
		}
	}
}

tasks {
	withType<KotlinCompile> {
		sourceCompatibility = "1.8"
		targetCompatibility = "1.8"

		kotlinOptions.jvmTarget = "1.8"
	}
}

afterEvaluate {
	val junitPlatformTest: JavaExec by tasks

	jacoco {
		applyTo(junitPlatformTest)
	}

	task<JacocoReport>("${junitPlatformTest.name}Report") {
		executionData(junitPlatformTest)

		val sourceSet = java.sourceSets["main"]
		sourceDirectories = files(sourceSet.allJava.srcDirs)
		classDirectories = files(sourceSet.output)

		reports {
			xml.destination = file("$buildDir/reports/jacoco.xml")
			xml.isEnabled = true
			html.isEnabled = false
		}

		tasks["check"].dependsOn(this)
	}
}

dependencies {
	api(kotlin("reflect", "1.1.60"))
	api(kotlin("stdlib-jre8", "1.1.60"))

	testImplementation("com.winterbe:expekt:0.5.0")
	testImplementation("org.jetbrains.spek:spek-subject-extension:1.1.5")
	testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
	testRuntime("org.junit.platform:junit-platform-runner:${junitPlatform.platformVersion}")

	"examplesImplementation"(java.sourceSets["main"].output)
}

configurations["examplesImplementation"].extendsFrom(configurations["api"])
configurations.all {
	resolutionStrategy.apply {
		force("org.jetbrains.kotlin:kotlin-reflect:1.1.60")
		force("org.jetbrains.kotlin:kotlin-stdlib:1.1.60")

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

val SourceSet.kotlin get() = (this as HasConvention).convention.getPlugin<KotlinSourceSet>().kotlin


// publishing

val javadoc = tasks["javadoc"] as Javadoc
val javadocJar by tasks.creating(Jar::class) {
	classifier = "javadoc"
	from(javadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
	classifier = "sources"
	from(java.sourceSets["main"].allSource)
}

publishing {
	(publications) {
		"default".invoke(MavenPublication::class) {
			from(components["java"])
			artifact(sourcesJar)
		}
	}
}

val ossrhUserName = findProperty("ossrhUserName") as String?
val ossrhPassword = findProperty("ossrhPassword") as String?
if (ossrhUserName != null && ossrhPassword != null) {
	artifacts {
		add("archives", javadocJar)
		add("archives", sourcesJar)
	}

	signing {
		sign(configurations.archives)
	}

	tasks {
		"uploadArchives"(Upload::class) {
			repositories {
				withConvention(MavenRepositoryHandlerConvention::class) {
					mavenDeployer {
						withGroovyBuilder {
							"beforeDeployment" { signing.signPom(delegate as MavenDeployment) }

							"repository"("url" to "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
								"authentication"("userName" to ossrhUserName, "password" to ossrhPassword)
							}

							"snapshotRepository"("url" to "https://oss.sonatype.org/content/repositories/snapshots/") {
								"authentication"("userName" to ossrhUserName, "password" to ossrhPassword)
							}
						}

						pom.project {
							withGroovyBuilder {
								"name"(project.name)
								"description"(project.description)
								"packaging"("jar")
								"url"("https://github.com/fluidsonic/fluid-json")
								"developers" {
									"developer" {
										"id"("fluidsonic")
										"name"("Marc Knaup")
										"email"("marc@knaup.koeln")
									}
								}
								"licenses" {
									"license" {
										"name"("MIT License")
										"url"("https://github.com/fluidsonic/fluid-json/blob/master/LICENSE")
									}
								}
								"scm" {
									"connection"("scm:git:https://github.com/fluidsonic/fluid-json.git")
									"developerConnection"("scm:git:git@github.com:fluidsonic/fluid-json.git")
									"url"("https://github.com/fluidsonic/fluid-json")
								}
							}
						}
					}
				}
			}
		}
	}
}

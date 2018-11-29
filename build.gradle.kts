import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = "A JSON library written in pure Kotlin."
group = "com.github.fluidsonic"
version = "0.9.6"


plugins {
	base
	kotlin("jvm") version "1.3.10" apply false
	`java-library`
	jacoco
	maven
	`maven-publish`
	signing
	id("com.github.ben-manes.versions") version "0.20.0"
}


allprojects {
	apply<KotlinPluginWrapper>()
	apply<JacocoPlugin>()
	apply<JavaLibraryPlugin>()

	sourceSets {
		getByName("main") {
			kotlin.srcDirs("sources")
			resources.srcDirs("resources")
		}

		getByName("test") {
			kotlin.srcDirs("tests/sources")
			resources.srcDirs("tests/resources")
		}
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	jacoco {
		toolVersion = "0.8.2"
	}

	tasks {
		val check by this

		withType<KotlinCompile> {
			sourceCompatibility = "1.8"
			targetCompatibility = "1.8"

			kotlinOptions.freeCompilerArgs = listOf("-Xuse-experimental=kotlin.contracts.ExperimentalContracts")
			kotlinOptions.jvmTarget = "1.8"
		}

		withType<Test> {
			useJUnitPlatform {
				includeEngines("spek2")
			}

			testLogging {
				events("FAILED", "PASSED", "SKIPPED")
				showExceptions = true
			}
		}

		withType<JacocoReport> {
			reports {
				xml.isEnabled = true
				html.isEnabled = false
			}

			check.dependsOn(this)
		}
	}

	dependencies {
		api(kotlin("stdlib-jdk8"))

		testImplementation("com.winterbe:expekt:0.5.0")
		testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.0-rc.1")
		testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.0-rc.1")
		testRuntimeOnly("org.junit.platform:junit-platform-runner:1.3.2")
	}

	configurations {
		all {
			resolutionStrategy {
				force("org.jetbrains.kotlin:kotlin-reflect:1.3.10")
				force("org.jetbrains.kotlin:kotlin-stdlib:1.3.10")
				force("org.jetbrains.kotlin:kotlin-stdlib-common:1.3.10")
				force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.10")
				force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10")
				force("org.junit.platform:junit-platform-engine:1.3.2")

				failOnVersionConflict()
			}
		}
	}

	repositories {
		mavenCentral()
		jcenter()
	}


	// publishing
	/*
	val javadoc = tasks["javadoc"] as Javadoc
	val javadocJar by tasks.creating(Jar::class) {
		classifier = "javadoc"
		from(javadoc)
	}

	val sourcesJar by tasks.creating(Jar::class) {
		classifier = "sources"
		from(sourceSets["main"].allSource)
	}

	publishing {
		publications {
			create<MavenPublication>("default") {
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
			sign(configurations.archives.get())
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
											"email"("marc@knaup.io")
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
	*/
}


val SourceSet.kotlin
	get() = withConvention(KotlinSourceSet::class) { kotlin }

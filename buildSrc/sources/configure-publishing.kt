import org.gradle.api.Project
import org.gradle.api.artifacts.maven.MavenDeployment
import org.gradle.api.plugins.MavenPlugin
import org.gradle.api.plugins.MavenRepositoryHandlerConvention
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.*
import org.gradle.plugins.signing.SigningPlugin


fun Project.configurePublishing(id: String) {
	apply<MavenPlugin>()
	apply<MavenPublishPlugin>()
	apply<PublishingPlugin>()
	apply<SigningPlugin>()

	val javadoc = tasks["javadoc"] as Javadoc
	val javadocJar by tasks.creating(Jar::class) {
		classifier = "javadoc"
		from(javadoc)
	}

	val sourcesJar by tasks.creating(Jar::class) {
		classifier = "sources"
		from(sourceSets["main"].allSource)
	}

	tasks.withType<Jar> {
		baseName = id
	}

	publishing {
		publications {
			register("mavenJava", MavenPublication::class) {
				artifactId = id

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

							pom {
								artifactId = id

								project {
									withGroovyBuilder {
										"name"(id)
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
	}
}

package tests.annotationprocessor

import io.fluidsonic.compiler.*
import io.fluidsonic.json.annotationprocessor.*
import org.jetbrains.kotlin.cli.common.*
import org.junit.jupiter.api.*
import java.io.*


internal object CaseTests {

	private fun buildTest(case: File) =
		DynamicTest.dynamicTest("case ${case.name}") {
			val actualOutputPath = case.resolve("output-actual")
			val expectedOutputPath = case.resolve("output-expected")

			val result = KotlinCompiler()
				.arguments {
					// TODO remove once fixed: https://youtrack.jetbrains.com/issue/KT-28011
					compileJava = false
					useJavac = false
				}
				.includesCurrentClasspath()
				.jvmTarget(KotlinJvmTarget.v1_8)
				.kaptOptions {
					sourcesOutputDir = actualOutputPath.also { it.deleteRecursively() }
				}
				.processors(AnnotationProcessor())
				.sources(case.resolve("input"))
				.compile()

			val errors = result.messages.filter { it.severity.isError }
			if (errors.isNotEmpty())
				fail(errors.joinToString(
					prefix = "annotation processing resulted in errors:\n----\n",
					separator = "\n",
					postfix = "\n----"
				) { it.toString().trimEnd() })

			if (result.exitCode != ExitCode.OK)
				fail("annotation processing failed with exit code ${result.exitCode}")

			val processedFiles = mutableSetOf<File>()

			expectedOutputPath.walkTopDown()
				.filter(File::isFile)
				.filterNot { it.name.startsWith(".") }
				.forEach { expectedFile ->
					val file = expectedFile.relativeTo(expectedOutputPath)
					val actualFile = actualOutputPath.resolve(file)
					if (!actualFile.exists())
						fail("expected output file '$file' does not exist in actual output")

					if (actualFile.readText().replace("\r", "") != expectedFile.readText().replace("\r", ""))
						fail("content of actual output file '$file' differs from expected output")

					processedFiles += file
				}

			actualOutputPath.walkTopDown()
				.filter(File::isFile)
				.filterNot { it.name.startsWith(".") }
				.forEach { actualFile ->
					val file = actualFile.relativeTo(actualOutputPath)
					if (!processedFiles.contains(file))
						fail("actual output contains unexpected file '$file'")
				}
		}!!


	private fun fail(message: String): Nothing =
		throw AssertionError(message)


	@TestFactory
	fun test() =
		File("test cases")
			.canonicalFile
			.listFiles()
			.orEmpty()
			.filter(File::isDirectory)
			.map(this::buildTest)
}

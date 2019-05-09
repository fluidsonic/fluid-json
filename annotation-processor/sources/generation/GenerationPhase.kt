package com.github.fluidsonic.fluid.json.annotationprocessor

import java.io.*


internal class GenerationPhase(
	private val outputDirectory: File,
	private val processingResult: ProcessingResult
) {

	private var isGenerated = false


	fun generate() {
		check(!isGenerated) { "can only generate once" }
		isGenerated = true

		CodecGenerator(outputDirectory = outputDirectory).run {
			processingResult.codecs.forEach(this::generate)
		}

		processingResult.codecProvider?.let { codecProvider ->
			CodecProviderGenerator(outputDirectory = outputDirectory)
				.generate(codecProvider, codecNames = processingResult.codecs.map { it.name })
		}
	}
}

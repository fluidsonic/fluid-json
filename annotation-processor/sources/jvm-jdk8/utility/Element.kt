package io.fluidsonic.json.annotationprocessor

import javax.lang.model.element.*


internal val Element.debugName: String
	get() {
		val components = mutableListOf<String>()

		var element: Element? = this
		while (element != null) {
			components += when (element) {
				is ExecutableElement -> "${element.simpleName}()"
				is PackageElement -> element.qualifiedName.toString()
				else -> element.simpleName.toString()
			}

			element = element.enclosingElement
		}

		components.reverse()

		return components.joinToString(".")
	}

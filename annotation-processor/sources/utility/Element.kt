package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement


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

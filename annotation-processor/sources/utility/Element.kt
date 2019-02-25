package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement


internal val Element.fullyQualifiedName: String
	get() {
		val components = mutableListOf<String>()

		var element: Element? = this
		while (element != null) {
			components += if (element is ExecutableElement)
				"${element.simpleName}()"
			else
				element.simpleName.toString()

			element = element.enclosingElement
		}

		components.reverse()

		return components.joinToString(".")
	}

package com.github.fluidsonic.fluid.json.annotationprocessor

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement


val Element.`package`: PackageElement
	get() = this as? PackageElement ?: enclosingElement?.`package` ?: error("element has no package: $this")

package com.github.fluidsonic.fluid.json


internal class JSONPathBuildingException(message: String, private val index: Int? = null) : RuntimeException(message) {

	private val pathComponents = mutableListOf<Any>()


	private fun addPathComponent(index: Int) {
		pathComponents += index
	}


	private fun addPathComponent(name: String) {
		pathComponents += name
	}


	private fun build(): JSONException {
		val exception = JSONException(buildString {
			index?.let {
				append("(UTF-16 offset #")
				append(it)
				append(")")
			}

			if (pathComponents.isNotEmpty()) {
				if (isNotEmpty()) {
					append(' ')
				}

				append("at ")
				append('\'')

				for (component in pathComponents.asReversed()) {
					when (component) {
						is Int -> {
							append('[')
							append(component)
							append(']')
						}

						else -> {
							append('.')
							append(component.toString())
						}
					}
				}

				append('\'')
			}

			if (isEmpty()) {
				append("root")
			}

			append(": ")
			append(message)
		})
		exception.stackTrace = stackTrace

		return exception
	}


	companion object {

		inline fun <Result> track(block: () -> Result) =
			try {
				block()
			}
			catch (e: JSONPathBuildingException) {
				throw e.build()
			}


		inline fun track(index: Int, block: () -> Unit) {
			try {
				block()
			}
			catch (e: JSONPathBuildingException) {
				e.addPathComponent(index)
				throw e
			}
		}


		inline fun track(name: String, block: () -> Unit) {
			try {
				block()
			}
			catch (e: JSONPathBuildingException) {
				e.addPathComponent(name)
				throw e
			}
		}
	}
}

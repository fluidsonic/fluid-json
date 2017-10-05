package com.github.fluidsonic.fluid.json


internal class JSONPathBuildingException(message: String) : RuntimeException(message) {

	private val pathComponents = mutableListOf<Any>()


	private fun addPathComponent(index: Int) {
		pathComponents += index
	}


	private fun addPathComponent(name: String) {
		pathComponents += name
	}


	private fun build(): JSONException {
		val exception = JSONException(buildString {
			if (pathComponents.isNotEmpty()) {
				append('\'')

				for ((index, component) in pathComponents.asReversed().withIndex()) {
					when (component) {
						is Int -> {
							append('[')
							append(component)
							append(']')
						}

						else -> {
							if (index > 0) {
								append('.')
							}

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

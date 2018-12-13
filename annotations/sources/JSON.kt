package com.github.fluidsonic.fluid.json


@Target()
@Retention(AnnotationRetention.SOURCE)
annotation class JSON {

	@Target(AnnotationTarget.CLASS)
	@Retention(AnnotationRetention.SOURCE)
	annotation class Codec(
		val name: String = "<default>",
		val packageName: String = "<default>",
		val visibility: Visibility = Visibility.DEFAULT
	)


	@Target(AnnotationTarget.FILE)
	@Retention(AnnotationRetention.SOURCE)
	annotation class CodecProvider(
		val name: String = "<default>",
		val packageName: String = "<default>",
		val visibility: Visibility = Visibility.DEFAULT
	)


	@Suppress("EnumEntryName")
	enum class Visibility {

		DEFAULT,
		INTERNAL,
		PUBLIC
	}
}

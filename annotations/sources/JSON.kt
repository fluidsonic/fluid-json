package com.github.fluidsonic.fluid.json

import com.github.fluidsonic.fluid.json.JSON.*
import kotlin.reflect.*


/**
 * Creates a [JSONCodec] for the annotated class.
 *
 * Note that there are several limitations which classes can be annotated:
 * - it and all enclosing classes must have `internal` or `public` visibility
 * - it must not be `abstract`, `sealed` or `inner`
 * - it must not be generic
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class JSON(
	/**
	 * Name of the generated codec class.
	 *
	 * By default its name will be derived from the name of the annotated class with `JSONCodec` appended and with the names of enclosing types prepended and
	 * separated by `_`.
	 *
	 * ### Examples:
	 * - `@JSON class Foo` -> `FooJSONCodec`
	 * - `class Foo { @JSON class Bar }` -> `Foo_BarJSONCodec`
	 */
	val codecName: String = automatic,

	/**
	 * Package name of the generated codec class.
	 *
	 * By default it will be located in the same package as the annotated class.
	 */
	val codecPackageName: String = automatic,

	/**
	 * Visibility of the generated codec class.
	 *
	 * By default the visibility will be [internal][CodecVisibility.internal].
	 */
	val codecVisibility: CodecVisibility = CodecVisibility.internal,

	/**
	 * Defines how to create an instance of the annotated class when decoding it from JSON.
	 *
	 * By default the [automatic][Decoding.automatic] approach will be used.
	 */
	val decoding: Decoding = Decoding.automatic,

	/**
	 * Defines how an instance of the annotated class will be encoded to JSON.
	 *
	 * By default the [automatic][Encoding.automatic] approach will be used.
	 */
	val encoding: Encoding = Encoding.automatic,

	/**
	 * Defines how the annotated class is represented in JSON.
	 *
	 * By default the representation will be determined [automatically][Representation.automatic].
	 */
	val representation: Representation = Representation.automatic
) {
	/**
	 * The annotated constructor of a class annotated with [@JSON][JSON] will be used for decoding instances of the class as JSON even if
	 * [@JSON.decoding][JSON.decoding] would exclude them by default or if there would be multiple constructors and a decision needs to be made. This does not
	 * apply if [@JSON.decoding][JSON.decoding] is set to [none][Decoding.none] in which case an error will be raised.
	 */
	@Retention(AnnotationRetention.SOURCE)
	@Target(AnnotationTarget.CONSTRUCTOR)
	annotation class Constructor


	/**
	 * Creates an object implementing the annotated interface which in turn extends [JSONCodecProvider] and no other interfaces. That object can be accessed
	 * using [`JSONCodecProvider.generated(AnnotatedInterface::class)`][JSONCodecProvider.Companion.generated]. It provides all codecs which have been generated
	 * using [@JSON][JSON] annotations. The annotated interface also specifies the [JSONCodingContext] type being used by all generated codecs.
	 *
	 * Note that only one codec provider can be created per module and it must not be generic.
	 *
	 * ### Example
	 *
	 * ```kotlin
	 * @JSON.CodecProvider
	 * interface ExampleJSONCodecProvider : JSONCodecProvider<MyCodingContext>
	 *
	 * val provider = JSONCodecProvider.generated(ExampleJSONCodecProvider::class)
	 * ```
	 */
	@Retention(AnnotationRetention.SOURCE)
	@Target(AnnotationTarget.CLASS)
	annotation class CodecProvider(
		/**
		 * Defines additional types for which codecs should be generated but which are not part of the same module as the annotated interface.
		 */
		val externalTypes: Array<JSON.ExternalType> = []
	)


	/**
	 * By annotating an extension function with this annotation additional code can be executed as part of the encoding of a [@JSON][JSON]-annotated class.
	 *
	 * This functionality requires the use of [@JSON.CodecProvider][CodecProvider] within the same module. The extension function must use [JSONEncoder] as
	 * receiver type with the same context type as the [@JSON.CodecProvider][CodecProvider]-annotated interface or a supertype of that context type.
	 *
	 * The annotated extension function must either be a member of the [@JSON][JSON]-annotated class or be defined at file-level and take exactly one value
	 * parameter of the [@JSON][JSON]-annotated class.
	 *
	 * Note that the function can have any name, its return value is ignored, it must have `internal` or `public` visibility and it must not be generic.
	 *
	 * ### Example
	 *
	 * #### Class member
	 *
	 * ```
	 * class User(
	 *     // …
	 *     val emailAddress: String
	 * ) {
	 *     @JSON.CustomProperties
	 *     fun JSONEncoder<AuthenticationAwareContext>.writeCustomProperties() {
	 *         if (this == context.authenticatedUser) {
	 *             writeMapElement("emailAddress", emailAddress)
	 *         }
	 *     }
	 * }
	 * ```
	 *
	 * #### File-level extension function
	 *
	 * ```
	 * @JSON.CustomProperties
	 * fun JSONEncoder<AuthenticationAwareContext>.writeCustomProperties(user: User) {
	 *     if (user == context.authenticatedUser) {
	 *         writeMapElement("emailAddress", user.emailAddress)
	 *     }
	 * }
	 * ```
	 */
	@Retention(AnnotationRetention.SOURCE)
	@Target(AnnotationTarget.FUNCTION)
	annotation class CustomProperties


	/**
	 * In a class annotated with [@JSON][JSON]
	 * - properties having this annotation will be ignored when encoding instances of that class and
	 * - constructors having this annotation will be ignored when decoding instances of that class.
	 */
	@Retention(AnnotationRetention.SOURCE)
	@Target(AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY)
	annotation class Excluded


	/**
	 * Creates a [JSONCodec] for a class which is not part of the same module and which thus cannot be annotated with [@JSON][JSON].
	 */
	@Retention(AnnotationRetention.SOURCE)
	@Target()
	annotation class ExternalType(
		/**
		 * The class for which a [JSONCodec] should be created.
		 *
		 * **You must also specify [targetName] if the class you reference is an inline class.**
		 */
		@Suppress("unused") // used through AnnotationMirror
		val target: KClass<*>,

		/**
		 * The [@JSON][JSON] annotation as if the [target] class was annotated directly.
		 */
		val configuration: JSON = JSON(),

		/**
		 * The fully qualified name of the class for which a [JSONCodec] should be created.
		 *
		 * **You must specify this property when using this annotation for inline classes.**
		 *
		 * This is a workaround for [KT-30280](https://youtrack.jetbrains.com/issue/KT-30280).
		 */
		val targetName: String = ""
	)


	/**
	 * Annotated properties of a class annotated with [@JSON][JSON] will be used for encoding instances of the class as JSON even if
	 * [@JSON.encoding][JSON.encoding] would exclude them by default. This does not apply if [@JSON.encoding][JSON.encoding] is set to [none][Encoding.none]
	 * in which case an error will be raised.
	 *
	 * This annotation can also be used to further configure how a specific property is being decoded and encoded.
	 */
	@Retention(AnnotationRetention.SOURCE)
	@Target(AnnotationTarget.PROPERTY) // TODO add & support AnnotationTarget.VALUE_PARAMETER once fixed: https://youtrack.jetbrains.com/issue/KT-30063
	annotation class Property(
		/**
		 * Defines the value of the JSON object key when decoding or encoding this property.
		 *
		 * By default this is the same as the property name in Kotlin.
		 */
		val serializedName: String = automatic
	)


	companion object {

		const val automatic: String = "<automatic>"
	}


	/**
	 * Visibility of a created codec
	 */
	enum class CodecVisibility {

		/**
		 * The visibility is derived automatically from the visibility of the annotated class as well as its enclosing classes.
		 */
		automatic,

		/**
		 * The visibility is always `internal`.
		 */
		internal,

		/**
		 * The visibility is always `public`.
		 *
		 * Note that this raises an error if the annotated class or one of its enclosing classes isn't `public`.
		 */
		publicRequired
	}


	/**
	 * The way in which the annotation processor can decode and instantiate a [@JSON][JSON]-annotated class.
	 */
	enum class Decoding {

		/**
		 * The constructor annotated with [@JSON.Constructor][Constructor] will be used for decoding and creating instances.
		 *
		 * If multiple constructors are or no constructor is annotated with [@JSON.Constructor][Constructor] then an error will be raised.
		 */
		annotatedConstructor,

		/**
		 * The annotation processor will try to find the best way to decode the [@JSON][JSON]-annotated class using the following approaches, in order:
		 *
		 * 1. `none` for any `object`
		 * 2. the constructor annotated with [@JSON.Constructor][Constructor] if present
		 * 3. the primary constructor if present, accessible and not annotated with [@JSON.Excluded][Excluded]
		 * 4. a secondary constructor if there is only one accessible and not annotated with [@JSON.Excluded][Excluded]
		 *
		 * Accessible in this case means having `internal` or `public` visibility.
		 *
		 * Note that an error will be raised if
		 * - multiple secondary constructors could be used and there is no primary constructor
		 * - the constructor which would be chosen uses `vararg`
		 * - none of the approaches can be used to create an instance of the class
		 */
		automatic,

		/**
		 * The annotated class is not decodable.
		 *
		 * Trying to use [@JSON.Constructor][Constructor] for such a non-decodable class raises an error.
		 */
		none,

		/**
		 * The class' primary constructor will be used for decoding.
		 *
		 * If there is no primary constructor, the constructor is not accessible, uses `vararg` or is annotated with [@JSON.Excluded][Excluded] then an error
		 * will be raised.
		 *
		 * Accessible in this case means having `internal` or `public` visibility.
		 */
		primaryConstructor
	}


	/**
	 * The way in which the annotation processor can encode a [@JSON][JSON]-annotated class.
	 */
	enum class Encoding {

		/**
		 * All properties defined in the class will be encoded unless either of the following is true:
		 *
		 * - property has been excluded using [@JSON.Excluded][Excluded]
		 * - property is not accessible
		 * - property is synthetic
		 * - property has a receiver parameter
		 *
		 * Accessible in this case means having `internal` or `public` visibility.
		 *
		 * File-level extension properties will also be included if annotated with [@JSON.Property][Property].
		 *
		 * If no properties are found then the class will be encoded as an empty object.
		 */
		allProperties,

		/**
		 * Only properties explicitly annotated with [@JSON.Property][Property] will be encoded.
		 *
		 * If no properties are found then the class will be encoded as an empty object.
		 */
		annotatedProperties,

		/**
		 * The annotation processor will try to find the best way to encode the [@JSON][JSON]-annotated class.
		 *
		 * This behaves like [allProperties] with the only difference being that properties who have a non-default getter are excluded by default.
		 */
		automatic,

		/**
		 * The annotated class is not encodable.
		 *
		 * Trying to use [@JSON.Property][Property] or [@JSON.CustomProperties][CustomProperties] for such a non-encodable class raises an error.
		 */
		none
	}


	/**
	 * The way in which the annotation processor will represent a [@JSON][JSON]-annotated class in JSON.
	 */
	enum class Representation {

		/**
		 * Classes will be represented [structured], except `inline` classes which will be represented as [singleValue].
		 */
		automatic,

		/**
		 * The annotated class will be represented directly as a single JSON value without wrapping that value in a JSON object.
		 *
		 * If the annotated class has zero or more than one encodable (if the class is encodable) or decodable (if the class is decodable) properties an error
		 * will be raised.
		 */
		singleValue,

		/**
		 * The annotated class will be represented as a JSON object and its properties being used as keys and value respectively.
		 */
		structured
	}
}

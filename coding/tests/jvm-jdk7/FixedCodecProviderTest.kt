package tests.coding

import io.fluidsonic.json.*
import kotlin.reflect.*
import kotlin.test.*


class FixedCodecProviderTest {

	@Test
	fun testDecodingMatchesExactInterface() {
		expect(
			provider(UnrelatedDecoderCodec, ParentDecoderCodec, ChildDecoderCodec)
				.decoderCodecForType<Parent, Context>()
		)
			.toBe(ParentDecoderCodec)
	}


	@Test
	fun testDecodingMatchesExactClass() {
		expect(
			provider(UnrelatedDecoderCodec, ChildDecoderCodec, ParentDecoderCodec)
				.decoderCodecForType<Child, Context>()
		)
			.toBe(ChildDecoderCodec)
	}


	@Test
	fun testDecodingObeysOrderOfCodecs() {
		expect(
			provider(UnrelatedDecoderCodec, ChildDecoderCodec2, ChildDecoderCodec)
				.decoderCodecForType<Child, Context>()
		)
			.toBe(ChildDecoderCodec2)
	}


	@Test
	fun testEncodingMatchesExactInterface() {
		expect(
			provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
				.encoderCodecForClass(Parent::class)
		)
			.toBe(ParentEncoderCodec)
	}


	@Test
	fun testEncodingMatchesExactClassType() {
		expect(
			provider(UnrelatedEncoderCodec, ChildEncoderCodec, ParentEncoderCodec)
				.encoderCodecForClass(Child::class)
		)
			.toBe(ChildEncoderCodec)
	}


	@Test
	fun testEncodingMatchesArrayTypes() {
		expect(
			provider(ArrayJsonCodec)
				.encoderCodecForClass(Array<Any?>::class)
		)
			.toBe(ArrayJsonCodec)

		expect(
			provider(ArrayJsonCodec)
				.encoderCodecForClass(Array<String>::class)
		)
			.toBe(ArrayJsonCodec)
	}


	@Test
	fun testEncodingMatchesSubclasses() {
		expect(
			provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
				.encoderCodecForClass(Child::class)
		)
			.toBe(ParentEncoderCodec)
	}


	@Test
	fun testEncodingMatchesPrimitiveArrayTypes() {
		expect(
			provider(ArrayJsonCodec)
				.encoderCodecForClass(IntArray::class as KClass<*>)
		)
			.toBe(null)
	}


	@Test
	fun testEncodingObeysOrderOfCodecs() {
		expect(
			provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
				.encoderCodecForClass(Child::class)
		)
			.toBe(ParentEncoderCodec)
	}


	private fun provider(vararg codecs: JsonDecoderCodec<*, Context>) =
		FixedCodecProvider(codecs.toList())


	private fun provider(vararg codecs: JsonEncoderCodec<*, Context>) =
		FixedCodecProvider(codecs.toList())


	interface Parent
	object Child : Parent
	object Context : JsonCodingContext
	interface Unrelated


	object ChildDecoderCodec : JsonDecoderCodec<Child, Context> {

		override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<Child>) = error("dummy")

		override val decodableType = jsonCodingType<Child>()
	}


	object ChildDecoderCodec2 : JsonDecoderCodec<Child, Context> {

		override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<Child>) = error("dummy")

		override val decodableType = jsonCodingType<Child>()
	}


	object ChildEncoderCodec : JsonEncoderCodec<Child, Context> {

		override fun JsonEncoder<Context>.encode(value: Child) = error("dummy")

		override val encodableClass = Child::class
	}


	object ParentDecoderCodec : JsonDecoderCodec<Parent, Context> {

		override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<Parent>) = error("dummy")

		override val decodableType = jsonCodingType<Parent>()
	}


	object ParentEncoderCodec : JsonEncoderCodec<Parent, Context> {

		override fun JsonEncoder<Context>.encode(value: Parent) = error("dummy")

		override val encodableClass = Parent::class
	}


	object UnrelatedDecoderCodec : JsonDecoderCodec<Unrelated, Context> {

		override fun JsonDecoder<Context>.decode(valueType: JsonCodingType<Unrelated>) = error("dummy")

		override val decodableType = jsonCodingType<Unrelated>()
	}


	object UnrelatedEncoderCodec : JsonEncoderCodec<Unrelated, Context> {

		override fun JsonEncoder<Context>.encode(value: Unrelated) = error("dummy")

		override val encodableClass = Unrelated::class
	}
}

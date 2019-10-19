package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.*
import ch.tutteli.atrium.verbs.*
import io.fluidsonic.json.*
import org.junit.jupiter.api.*
import kotlin.reflect.*


internal class FixedCodecProviderTest {

	@Nested
	inner class DecodingTest {

		@Test
		fun testMatchesExactInterface() {
			assert(
				provider(UnrelatedDecoderCodec, ParentDecoderCodec, ChildDecoderCodec)
					.decoderCodecForType<Parent, Context>()
			)
				.toBe(ParentDecoderCodec)
		}


		@Test
		fun testMatchesExactClass() {
			assert(
				provider(UnrelatedDecoderCodec, ChildDecoderCodec, ParentDecoderCodec)
					.decoderCodecForType<Child, Context>()
			)
				.toBe(ChildDecoderCodec)
		}


		@Test
		fun testObeysOrderOfCodecs() {
			assert(
				provider(UnrelatedDecoderCodec, ChildDecoderCodec2, ChildDecoderCodec)
					.decoderCodecForType<Child, Context>()
			)
				.toBe(ChildDecoderCodec2)
		}
	}


	@Nested
	inner class EncodingTest {

		@Test
		fun testMatchesExactInterface() {
			assert(
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Parent::class)
			)
				.toBe(ParentEncoderCodec)
		}


		@Test
		fun testMatchesExactClassType() {
			assert(
				provider(UnrelatedEncoderCodec, ChildEncoderCodec, ParentEncoderCodec)
					.encoderCodecForClass(Child::class)
			)
				.toBe(ChildEncoderCodec)
		}


		@Test
		fun testMatchesArrayTypes() {
			assert(
				provider(ArrayJsonCodec)
					.encoderCodecForClass(Array<Any?>::class)
			)
				.toBe(ArrayJsonCodec)

			assert(
				provider(ArrayJsonCodec)
					.encoderCodecForClass(Array<String>::class)
			)
				.toBe(ArrayJsonCodec)
		}


		@Test
		fun testMatchesSubclasses() {
			assert(
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Child::class)
			)
				.toBe(ParentEncoderCodec)
		}


		@Test
		fun testMatchesPrimitiveArrayTypes() {
			assert(
				provider(ArrayJsonCodec)
					.encoderCodecForClass(IntArray::class as KClass<*>)
			)
				.toBe(null)
		}


		@Test
		fun testObeysOrderOfCodecs() {
			assert(
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Child::class)
			)
				.toBe(ParentEncoderCodec)
		}
	}


	private fun provider(vararg codecs: JsonDecoderCodec<*, FixedCodecProviderTest.Context>) =
		FixedCodecProvider(codecs.toList())


	private fun provider(vararg codecs: JsonEncoderCodec<*, FixedCodecProviderTest.Context>) =
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

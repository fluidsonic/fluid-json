package tests.coding

import ch.tutteli.atrium.api.cc.en_GB.notToBeNullBut
import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.assert
import com.github.fluidsonic.fluid.json.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass


internal class FixedCodecProviderTest {

	@Nested
	inner class DecodingTest {

		@Test
		fun testMatchesExactInterface() {
			assert(
				provider(UnrelatedDecoderCodec, ParentDecoderCodec, ChildDecoderCodec)
					.decoderCodecForType<Parent, Context>()
			)
				.notToBeNullBut(ParentDecoderCodec)
		}


		@Test
		fun testMatchesExactClass() {
			assert(
				provider(UnrelatedDecoderCodec, ChildDecoderCodec, ParentDecoderCodec)
					.decoderCodecForType<Child, Context>()
			)
				.notToBeNullBut(ChildDecoderCodec)
		}


		@Test
		fun testObeysOrderOfCodecs() {
			assert(
				provider(UnrelatedDecoderCodec, ChildDecoderCodec2, ChildDecoderCodec)
					.decoderCodecForType<Child, Context>()
			)
				.notToBeNullBut(ChildDecoderCodec2)
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
				.notToBeNullBut(ParentEncoderCodec)
		}


		@Test
		fun testMatchesExactClassType() {
			assert(
				provider(UnrelatedEncoderCodec, ChildEncoderCodec, ParentEncoderCodec)
					.encoderCodecForClass(Child::class)
			)
				.notToBeNullBut(ChildEncoderCodec)
		}


		@Test
		fun testMatchesArrayTypes() {
			assert(
				provider(ArrayJSONCodec)
					.encoderCodecForClass(Array<Any?>::class)
			)
				.notToBeNullBut(ArrayJSONCodec)

			assert(
				provider(ArrayJSONCodec)
					.encoderCodecForClass(Array<String>::class)
			)
				.notToBeNullBut(ArrayJSONCodec)
		}


		@Test
		fun testMatchesSubclasses() {
			assert(
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Child::class)
			)
				.notToBeNullBut(ParentEncoderCodec)
		}


		@Test
		fun testMatchesPrimitiveArrayTypes() {
			assert(
				provider(ArrayJSONCodec)
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
				.notToBeNullBut(ParentEncoderCodec)
		}
	}


	private fun provider(vararg codecs: JSONDecoderCodec<*, FixedCodecProviderTest.Context>) =
		FixedCodecProvider(codecs.toList())


	private fun provider(vararg codecs: JSONEncoderCodec<*, FixedCodecProviderTest.Context>) =
		FixedCodecProvider(codecs.toList())


	interface Parent
	object Child : Parent
	object Context : JSONCodingContext
	interface Unrelated


	object ChildDecoderCodec : JSONDecoderCodec<Child, Context> {

		override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<in Child>) = error("dummy")

		override val decodableType = jsonCodingType<Child>()
	}


	object ChildDecoderCodec2 : JSONDecoderCodec<Child, Context> {

		override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<in Child>) = error("dummy")

		override val decodableType = jsonCodingType<Child>()
	}


	object ChildEncoderCodec : JSONEncoderCodec<Child, Context> {

		override fun JSONEncoder<Context>.encode(value: Child) = error("dummy")

		override val encodableClass = Child::class
	}


	object ParentDecoderCodec : JSONDecoderCodec<Parent, Context> {

		override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<in Parent>) = error("dummy")

		override val decodableType = jsonCodingType<Parent>()
	}


	object ParentEncoderCodec : JSONEncoderCodec<Parent, Context> {

		override fun JSONEncoder<Context>.encode(value: Parent) = error("dummy")

		override val encodableClass = Parent::class
	}


	object UnrelatedDecoderCodec : JSONDecoderCodec<Unrelated, Context> {

		override fun JSONDecoder<Context>.decode(valueType: JSONCodingType<in Unrelated>) = error("dummy")

		override val decodableType = jsonCodingType<Unrelated>()
	}


	object UnrelatedEncoderCodec : JSONEncoderCodec<Unrelated, Context> {

		override fun JSONEncoder<Context>.encode(value: Unrelated) = error("dummy")

		override val encodableClass = Unrelated::class
	}
}
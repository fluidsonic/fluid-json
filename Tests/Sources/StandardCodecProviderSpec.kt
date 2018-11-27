package tests

import com.github.fluidsonic.fluid.json.*
import com.winterbe.expekt.should
import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.TestBody
import org.spekframework.spek2.style.specification.describe
import kotlin.reflect.KClass


internal object StandardCodecProviderSpec : Spek({

	describe("StandardCodecProvider") {

		describe("for decoding") {

			it("finds a codec by exact interface type") {
				provider(UnrelatedDecoderCodec, ParentDecoderCodec, ChildDecoderCodec)
					.decoderCodecForType<Parent, Context>()
					.should.equal(ParentDecoderCodec)
			}

			it("finds a codec by exact class type") {
				provider(UnrelatedDecoderCodec, ChildDecoderCodec, ParentDecoderCodec)
					.decoderCodecForType<Child, Context>()
					.should.equal(ChildDecoderCodec)
			}

			it("obeys order of codecs") {
				provider(UnrelatedDecoderCodec, ChildDecoderCodec2, ChildDecoderCodec)
					.decoderCodecForType<Child, Context>()
					.should.equal(ChildDecoderCodec2)
			}
		}


		describe("for encoding") {

			it("finds a codec by exact interface type") {
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Parent::class)
					.should.equal(ParentEncoderCodec)
			}

			it("finds a codec by exact class type") {
				provider(UnrelatedEncoderCodec, ChildEncoderCodec, ParentEncoderCodec)
					.encoderCodecForClass(Child::class)
					.should.equal(ChildEncoderCodec)
			}

			it("finds a codec for array type") {
				provider(ArrayJSONCodec).encoderCodecForClass(Array<Any?>::class).should.equal(ArrayJSONCodec)
				provider(ArrayJSONCodec).encoderCodecForClass(Array<String>::class).should.equal(ArrayJSONCodec)
			}

			it("finds a codec for subclasses / implementing classes") {
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Child::class)
					.should.equal(ParentEncoderCodec)
			}

			it("finds no object array codec for primitive array type") {
				provider(ArrayJSONCodec).encoderCodecForClass(IntArray::class as KClass<*>).should.be.`null`
			}

			it("obeys order of codecs") {
				provider(UnrelatedEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Child::class)
					.should.equal(ParentEncoderCodec)
			}
		}
	}
}) {

	interface Parent

	object Child : Parent

	object Context : JSONCoderContext

	interface Unrelated


	object ChildDecoderCodec : JSONDecoderCodec<Child, Context> {

		override fun decode(valueType: JSONCodableType<in Child>, decoder: JSONDecoder<Context>) = error("dummy")

		override val decodableType = jsonCodableType<Child>()
	}


	object ChildDecoderCodec2 : JSONDecoderCodec<Child, Context> {

		override fun decode(valueType: JSONCodableType<in Child>, decoder: JSONDecoder<Context>) = error("dummy")

		override val decodableType = jsonCodableType<Child>()
	}


	object ChildEncoderCodec : JSONEncoderCodec<Child, Context> {

		override fun encode(value: Child, encoder: JSONEncoder<Context>) = error("dummy")

		override val encodableClass = Child::class
	}


	object ParentDecoderCodec : JSONDecoderCodec<Parent, Context> {

		override fun decode(valueType: JSONCodableType<in Parent>, decoder: JSONDecoder<Context>) = error("dummy")

		override val decodableType = jsonCodableType<Parent>()
	}


	object ParentEncoderCodec : JSONEncoderCodec<Parent, Context> {

		override fun encode(value: Parent, encoder: JSONEncoder<Context>) = error("dummy")

		override val encodableClass = Parent::class
	}


	object UnrelatedDecoderCodec : JSONDecoderCodec<Unrelated, Context> {

		override fun decode(valueType: JSONCodableType<in Unrelated>, decoder: JSONDecoder<Context>) = error("dummy")

		override val decodableType = jsonCodableType<Unrelated>()
	}


	object UnrelatedEncoderCodec : JSONEncoderCodec<Unrelated, Context> {

		override fun encode(value: Unrelated, encoder: JSONEncoder<Context>) = error("dummy")

		override val encodableClass = Unrelated::class
	}
}


// TODO move the following method inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private fun TestBody.provider(vararg codecs: JSONDecoderCodec<*, StandardCodecProviderSpec.Context>) =
	StandardCodecProvider(codecs.toList())


@Suppress("unused")
private fun TestBody.provider(vararg codecs: JSONEncoderCodec<*, StandardCodecProviderSpec.Context>) =
	StandardCodecProvider(codecs.toList())

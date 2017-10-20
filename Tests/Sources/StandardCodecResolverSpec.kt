package tests

import com.github.fluidsonic.fluid.json.ArrayJSONCodec
import com.github.fluidsonic.fluid.json.JSONCoderContext
import com.github.fluidsonic.fluid.json.JSONDecoder
import com.github.fluidsonic.fluid.json.JSONDecoderCodec
import com.github.fluidsonic.fluid.json.JSONEncoder
import com.github.fluidsonic.fluid.json.JSONEncoderCodec
import com.github.fluidsonic.fluid.json.StandardCodecResolver
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.TestBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


internal object StandardCodecResolverSpec : Spek({

	describe("StandardCodecResolver") {

		describe("for decoding") {

			it("finds a codec by exact interface type") {
				resolver(NothingDecoderCodec, ParentDecoderCodec, ChildDecoderCodec)
					.decoderCodecForClass(Parent::class.java)
					.should.equal(ParentDecoderCodec)
			}

			it("finds a codec by exact class type") {
				resolver(NothingDecoderCodec, ChildDecoderCodec, ParentDecoderCodec)
					.decoderCodecForClass(Child::class.java)
					.should.equal(ChildDecoderCodec)
			}

			it("finds a codec for subclasses / implementing classes") {
				resolver(NothingDecoderCodec, ChildDecoderCodec)
					.decoderCodecForClass(Parent::class.java)
					.should.equal(ChildDecoderCodec)
			}

			it("obeys order of codecs") {
				resolver(NothingDecoderCodec, ChildDecoderCodec, ParentDecoderCodec)
					.decoderCodecForClass(Parent::class.java)
					.should.equal(ChildDecoderCodec)
			}
		}


		describe("for encoding") {

			it("finds a codec by exact interface type") {
				resolver(NothingEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Parent::class.java)
					.should.equal(ParentEncoderCodec)
			}

			it("finds a codec by exact class type") {
				resolver(NothingEncoderCodec, ChildEncoderCodec, ParentEncoderCodec)
					.encoderCodecForClass(Child::class.java)
					.should.equal(ChildEncoderCodec)
			}

			it("finds a codec for array type") {
				resolver(ArrayJSONCodec).encoderCodecForClass(Array<Any?>::class.java).should.equal(ArrayJSONCodec)
				resolver(ArrayJSONCodec).encoderCodecForClass(Array<String>::class.java).should.equal(ArrayJSONCodec)
			}

			it("finds a codec for superclasses / interface") {
				resolver(NothingEncoderCodec, ParentEncoderCodec)
					.encoderCodecForClass(Child::class.java)
					.should.equal(ParentEncoderCodec)
			}

			it("finds no object array codec for primitive array type") {
				resolver(ArrayJSONCodec).encoderCodecForClass(IntArray::class.java as Class<*>).should.be.`null`
			}

			it("obeys order of codecs") {
				resolver(NothingEncoderCodec, ParentEncoderCodec, ChildEncoderCodec)
					.encoderCodecForClass(Child::class.java)
					.should.equal(ParentEncoderCodec)
			}
		}
	}
}) {

	interface Parent

	object Child : Parent

	object Context : JSONCoderContext


	object ChildDecoderCodec : JSONDecoderCodec<Child, Context> {

		override fun decode(decoder: JSONDecoder<Context>) = error("dummy")

		override val valueClass = Child::class.java
	}


	object ChildEncoderCodec : JSONEncoderCodec<Child, Context> {

		override fun encode(value: Child, encoder: JSONEncoder<Context>) = error("dummy")

		override val valueClass = Child::class.java
	}


	object ParentDecoderCodec : JSONDecoderCodec<Parent, Context> {

		override fun decode(decoder: JSONDecoder<Context>) = error("dummy")

		override val valueClass = Parent::class.java
	}


	object ParentEncoderCodec : JSONEncoderCodec<Parent, Context> {

		override fun encode(value: Parent, encoder: JSONEncoder<Context>) {
			value.should.be.instanceof(Parent::class.java)
		}

		override val valueClass = Parent::class.java
	}


	object NothingDecoderCodec : JSONDecoderCodec<Nothing, Context> {

		override fun decode(decoder: JSONDecoder<Context>) = error("dummy")

		override val valueClass = Nothing::class.java
	}


	object NothingEncoderCodec : JSONEncoderCodec<Nothing, Context> {

		override fun encode(value: Nothing, encoder: JSONEncoder<Context>) = error("dummy")

		override val valueClass = Nothing::class.java
	}
}


// TODO move the following method inside the object above once KT-19796 is fixed
// https://youtrack.jetbrains.com/issue/KT-19796

@Suppress("unused")
private fun TestBody.resolver(vararg codecs: JSONDecoderCodec<*, StandardCodecResolverSpec.Context>) =
	StandardCodecResolver(decoderCodecs = codecs.toList(), encoderCodecs = emptyList())


@Suppress("unused")
private fun TestBody.resolver(vararg codecs: JSONEncoderCodec<*, StandardCodecResolverSpec.Context>) =
	StandardCodecResolver(decoderCodecs = emptyList(), encoderCodecs = codecs.toList())

package tests

import com.github.fluidsonic.fluid.json.JSONSerializer
import com.github.fluidsonic.fluid.json.StandardSerializer
import com.winterbe.expekt.should
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek


internal object StandardSerializerAcceptSpec : SubjectSpek<JSONSerializer>({

	subject { StandardSerializer() }


	describe("StandardSerializer serializes") {

		describe("constants") {

			it("null") {
				subject.serialize(null).should.equal("null")
			}

			it("true") {
				subject.serialize(true).should.equal("true")
			}

			it("false") {
				subject.serialize(false).should.equal("false")
			}
		}


		describe("byte") {

			it("zero") {
				subject.serialize(0.toByte()).should.equal("0")
			}

			it("positive") {
				subject.serialize(100.toByte()).should.equal("100")
			}

			it("negative") {
				subject.serialize((-100).toByte()).should.equal("-100")
			}
		}


		describe("short") {

			it("zero") {
				subject.serialize(0.toShort()).should.equal("0")
			}

			it("positive") {
				subject.serialize(100.toShort()).should.equal("100")
			}

			it("negative") {
				subject.serialize((-100).toShort()).should.equal("-100")
			}
		}


		describe("integer") {

			it("zero") {
				subject.serialize(0).should.equal("0")
			}

			it("positive") {
				subject.serialize(100).should.equal("100")
			}

			it("negative") {
				subject.serialize(-100).should.equal("-100")
			}
		}


		describe("long") {

			it("if too large positive number for integer") {
				subject.serialize(2147483648L).should.equal("2147483648")
			}

			it("if too large negative number for integer") {
				subject.serialize(-2147483649L).should.equal("-2147483649")
			}
		}


		describe("float") {

			it("zero") {
				subject.serialize(0.0f).should.equal("0.0")
			}

			it("positive") {
				subject.serialize(100.001f).should.equal("100.001")
			}

			it("negative") {
				subject.serialize(-0.000f).should.equal("-0.0")
				subject.serialize(-100.001f).should.equal("-100.001")
			}

			it("with positive exponent sign") {
				subject.serialize(1e20f).should.equal("1.0E20")
			}

			it("with negative exponent sign") {
				subject.serialize(1e-20f).should.equal("1.0E-20")
			}
		}


		describe("double") {

			it("zero") {
				subject.serialize(0.0).should.equal("0.0")
			}

			it("positive") {
				subject.serialize(100.001).should.equal("100.001")
			}

			it("negative") {
				subject.serialize(-0.000).should.equal("-0.0")
				subject.serialize(-100.001).should.equal("-100.001")
			}

			it("with positive exponent sign") {
				subject.serialize(1e200).should.equal("1.0E200")
			}

			it("with negative exponent sign") {
				subject.serialize(1e-200).should.equal("1.0E-200")
			}
		}


		describe("string") {

			it("empty") {
				subject.serialize("").should.equal("\"\"")
			}

			it("simple") {
				subject.serialize("simple").should.equal("\"simple\"")
			}

			it("a bit longer") {
				subject.serialize(" a bit longer ").should.equal("\" a bit longer \"")
			}

			it("with emojis") {
				subject.serialize("a dog: 🐶").should.equal("\"a dog: 🐶\"")
			}

			it("with quotation mark and reverse solidus") {
				subject.serialize("\\ \"").should.equal("\"\\\\ \\\"\"")
			}

			it("with control characters") {
				subject.serialize("\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u000B\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u0020").should.equal("\"\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u000B\\u000E\\u000F\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F\u0020\"")
			}
		}


		describe("list") {

			it("empty") {
				subject.serialize(emptyList<Any>()).should.equal("[]")
			}

			it("with single element") {
				subject.serialize(listOf(1)).should.equal("[1]")
			}

			it("with multiple elements") {
				subject.serialize(listOf(true, "hey", null)).should.equal("[true,\"hey\",null]")
			}

			it("with nested arrays") {
				subject.serialize(listOf(emptyList<Any>(), listOf(1))).should.equal("[[],[1]]")
			}
		}


		describe("iterable") {

			class TestIterable<out Element>(base: Iterable<Element>) : Iterable<Element> by base


			it("empty") {
				subject.serialize(TestIterable(emptyList<Any?>())).should.equal("[]")
			}

			it("with single element") {
				subject.serialize(TestIterable(listOf(1))).should.equal("[1]")
			}

			it("with multiple elements") {
				subject.serialize(TestIterable(listOf(true, "hey", null))).should.equal("[true,\"hey\",null]")
			}

			it("with nested arrays") {
				subject.serialize(TestIterable(listOf(emptyList<Any>(), listOf(1)))).should.equal("[[],[1]]")
			}
		}


		describe("map") {

			it("empty") {
				subject.serialize(emptyMap<String, Any>()).should.equal("{}")
			}

			it("with single element") {
				subject.serialize(mapOf("key" to 1)).should.equal("{\"key\":1}")
			}

			it("with multiple elements") {
				subject.serialize(mapOf(
					"key0" to true,
					"key1" to "hey",
					"key2" to null
				)).should.equal("{\"key0\":true,\"key1\":\"hey\",\"key2\":null}")
			}

			it("with nested objects") {
				subject.serialize(mapOf(
					"key0" to emptyMap<String, Any>(),
					"key1" to mapOf("key" to 1)
				)).should.equal("{\"key0\":{},\"key1\":{\"key\":1}}")
			}

			it("complex strings as key") {
				subject.serialize(mapOf(
					" \\ \" / \b \u000C \n \r \t 🐶 " to 1
				)).should.equal("{\" \\\\ \\\" / \\b \\f \\n \\r \\t 🐶 \":1}")
			}

			it("maintaining element order") {
				subject.serialize(mapOf(
					"0" to 0,
					"2" to 2,
					"1" to 1,
					"3" to 3,
					"-1" to -1
				)).should.equal("{\"0\":0,\"2\":2,\"1\":1,\"3\":3,\"-1\":-1}")
			}
		}


		it("a complete example") {
			subject.serialize(mapOf(
				"true" to true,
				"false" to false,
				"null" to null,
				"1" to 1,
				"-1.0e-2" to -1.0e-2,
				"array" to listOf(
					true,
					false,
					null,
					1,
					-1.0e-2,
					listOf("non-empty"),
					mapOf(
						"true" to true,
						"false" to false,
						"null" to null,
						"1" to 1,
						"-1.0e-2" to -1.0e-2,
						"array" to listOf("non-empty"),
						"object" to mapOf("non" to "empty")
					)
				),
				"object" to mapOf("non" to "empty")
			)).should.equal("""
			{
				"true":    true,
				"false":   false,
				"null":    null,
				"1":       1,
				"-1.0e-2": -0.01,
				"array":   [
					true,
					false,
					null,
					1,
					-0.01,
					["non-empty"],
					{
						"true":    true,
						"false":   false,
						"null":    null,
						"1":       1,
						"-1.0e-2": -0.01,
						"array":   ["non-empty"],
						"object":  { "non": "empty" }
					}
				],
				"object": { "non": "empty" }
			}
			""".filterNot(Char::isWhitespace))
		}


		describe("transforms invalid keys & values to string") {

			subject { StandardSerializer(convertsInvalidValuesToString = true, convertsInvalidKeysToString = true) }


			it("returns correct conversion settings") {
				StandardSerializer().convertsInvalidKeysToString.should.be.`false`
				StandardSerializer().convertsInvalidValuesToString.should.be.`false`
				StandardSerializer(convertsInvalidKeysToString = false).convertsInvalidKeysToString.should.be.`false`
				StandardSerializer(convertsInvalidValuesToString = false).convertsInvalidValuesToString.should.be.`false`
				StandardSerializer(convertsInvalidKeysToString = true).convertsInvalidKeysToString.should.be.`true`
				StandardSerializer(convertsInvalidValuesToString = true).convertsInvalidValuesToString.should.be.`true`
			}

			it("non-finite float") {
				subject.serialize(Float.NEGATIVE_INFINITY).should.equal("\"-Infinity\"")
				subject.serialize(Float.POSITIVE_INFINITY).should.equal("\"Infinity\"")
				subject.serialize(Float.NaN).should.equal("\"NaN\"")
			}

			it("non-finite double") {
				subject.serialize(Double.NEGATIVE_INFINITY).should.equal("\"-Infinity\"")
				subject.serialize(Double.POSITIVE_INFINITY).should.equal("\"Infinity\"")
				subject.serialize(Double.NaN).should.equal("\"NaN\"")
			}

			it("non-string map keys") {
				subject.serialize(mapOf(null to null)).should.equal("{\"null\":null}")
				subject.serialize(mapOf(0 to 0)).should.equal("{\"0\":0}")
			}

			it("unsupported classes") {
				val obj = object {
					override fun toString() = "object"
				}

				subject.serialize(obj).should.equal("\"object\"")
			}
		}
	}
})

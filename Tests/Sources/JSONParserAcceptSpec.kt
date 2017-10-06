package tests

import com.github.fluidsonic.fluid.json.JSONParser
import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it


class JSONParserAcceptSpec : Spek({

	describe("JSONParser accepts") {

		describe("constants") {

			it("null") {
				parse("null").should.be.`null`
			}

			it("true") {
				parse("true").should.equal(true)
			}

			it("false") {
				parse("false").should.equal(false)
			}
		}


		describe("integer") {

			it("zero") {
				parse("0").should.equal(0)
			}

			it("positive") {
				parse("100").should.equal(100)
			}

			it("negative") {
				parse("-0").should.equal(0)
				parse("-100").should.equal(-100)
			}
		}


		describe("long") {

			it("if too large positive number for integer") {
				parse("2147483648").should.equal(2147483648L)
			}

			it("if too large negative number for integer") {
				parse("-2147483649").should.equal(-2147483649L)
			}
		}


		describe("double") {

			describe("from decimal") {

				it("zero") {
					parse("0.0").should.equal(0.0)
				}

				it("positive") {
					parse("100.001").should.equal(100.001)
				}

				it("negative") {
					parse("-0.000").should.equal(-0.0)
					parse("-100.001").should.equal(-100.001)
				}

				it("positive with exponent") {
					parse("1.0e2").should.equal(100.0)
				}

				it("negative with exponent") {
					parse("-1.0e2").should.equal(-100.0)
				}
			}


			describe("from integral") {

				it("if too large positive number for long") {
					parse("9223372036854775808").should.equal(9223372036854775808.0)
				}

				it("if too large negative number for long") {
					parse("-9223372036854775809").should.equal(-9223372036854775809.0)
				}

				it("if really large") {
					parse("1000000000000000000000000000000").should.equal(1000000000000000000000000000000.0)
				}

				it("if really small") {
					parse("-1000000000000000000000000000000").should.equal(-1000000000000000000000000000000.0)
				}

				it("positive with exponent") {
					parse("1e2").should.equal(100.0)
				}

				it("negative with exponent") {
					parse("-1e2").should.equal(-100.0)
				}

				it("as positive infinity if too large positive number for double") {
					parse("1e20000").should.equal(Double.POSITIVE_INFINITY)
				}

				it("as negative infinity if too large negative number for double") {
					parse("-1e20000").should.equal(Double.NEGATIVE_INFINITY)
				}
			}

			it("with positive exponent sign") {
				parse("1e+2").should.equal(100.0)
			}

			it("with negative exponent sign") {
				parse("1e-2").should.equal(0.01)
			}

			it("as zero if too small for double") {
				parse("1e-20000").should.equal(0.0)
			}

			it("with uppercase exponent separator") {
				parse("1E2").should.equal(100.0)
			}
		}


		describe("string") {

			it("empty") {
				parse("\"\"").should.equal("")
			}

			it("simple") {
				parse("\"simple\"").should.equal("simple")
			}

			it("a bit longer") {
				parse("\" a bit longer \"").should.equal(" a bit longer ")
			}

			it("with emojis") {
				parse("\"a dog: 🐶\"").should.equal("a dog: 🐶")
			}

			it("with escape sequences") {
				parse("\" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \"").should.equal(" \\ \" / \b \u000C \n \r \t 🐶 ")
			}
		}


		describe("array") {

			it("empty") {
				parse("[]").should.equal(emptyList<Any>())
			}

			it("empty with whitespace") {
				parse("[ \t\n\r]").should.equal(emptyList<Any>())
			}

			it("with single element") {
				parse("[1]").should.equal(listOf(1))
			}

			it("with multiple elements") {
				parse("[ true, \"hey\", null ]").should.equal(listOf(true, "hey", null))
			}

			it("with nested arrays") {
				parse("[ [], [ 1 ] ]").should.equal(listOf(emptyList<Any>(), listOf(1)))
			}
		}


		describe("object") {

			it("empty") {
				parse("{}").should.equal(emptyMap<String, Any>())
			}

			it("empty with whitespace") {
				parse("{ \t\n\r}").should.equal(emptyMap<String, Any>())
			}

			it("with single element") {
				parse("{\"key\":1}").should.equal(mapOf("key" to 1))
			}

			it("with multiple elements") {
				parse("{ \"key0\": true, \"key1\" :\"hey\", \"key2\" : null }").should.equal(mapOf(
					"key0" to true,
					"key1" to "hey",
					"key2" to null
				))
			}

			it("with nested objects") {
				parse("{ \"key0\": {}, \"key1\": { \"key\": 1 } }").should.equal(mapOf(
					"key0" to emptyMap<String, Any>(),
					"key1" to mapOf("key" to 1)
				))
			}

			it("complex strings as key") {
				parse("{ \" \\\\ \\\" \\/ \\b \\f \\n \\r \\t \\uD83D\\udc36 \": 1 }").should.equal(mapOf(
					" \\ \" / \b \u000C \n \r \t 🐶 " to 1
				))
			}

			it("maintaining element order") {
				parseMap("{ \"0\": 0, \"2\": 2, \"1\": 1, \"3\": 3, \"-1\": -1 }").toList().should.equal(listOf(
					"0" to 0,
					"2" to 2,
					"1" to 1,
					"3" to 3,
					"-1" to -1
				))
			}
		}


		it("a complete example") {
			parse("""
			{
				"true":    true,
				"false":   false,
				"null":    null,
				"1":       1,
				"-1.0e-2": -1.0e-2,
				"array":   [
					true,
					false,
					null,
					1,
					-1.0e-2,
					["non-empty"],
					{
						"true":    true,
						"false":   false,
						"null":    null,
						"1":       1,
						"-1.0e-2": -1.0e-2,
						"array":   ["non-empty"],
						"object":  { "non": "empty" }
					}
				],
				"object": { "non": "empty" }
			}
			""").should.equal(mapOf(
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
			))
		}


		it("a list as expected") {
			parseList("[1]").should.equal(listOf(1))
		}


		it("a map as expected") {
			parseMap("{\"key\":1}").should.equal(mapOf("key" to 1))
		}
	}
}) {

	private companion object {

		fun parse(string: String) =
			JSONParser().parse(string)

		fun parseList(string: String) =
			JSONParser().parseList(string)

		fun parseMap(string: String) =
			JSONParser().parseMap(string)
	}
}

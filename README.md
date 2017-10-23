fluid-json
==========

[![Build Status](https://travis-ci.org/fluidsonic/fluid-json.svg?branch=master)](https://travis-ci.org/fluidsonic/fluid-json)
[![codecov.io](https://codecov.io/github/fluidsonic/fluid-json/coverage.svg?branch=master)](https://codecov.io/github/fluidsonic/fluid-json?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.fluidsonic/fluid-json.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.fluidsonic%22%20a%3A%22fluid-json%22)

A JSON library written in pure Kotlin.



Installation
------------

This library is [available in Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.fluidsonic%22%20a%3A%22fluid-json%22) as `fluid-json` in the group `com.github.fluidsonic`.

`build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.fluidsonic:fluid-json:0.0.2")
}
```


Examples
--------

Check out the [Examples](https://github.com/fluidsonic/fluid-json/tree/master/Examples) directory. If you've checked out
this project locally then you can run them directly from within IntelliJ.



Usage
-----

### Simple Parsing

```kotlin
… = JSONParser.default().parseValue("""{ "hello": "world", "test": 123 }""")

// returns a value like this:
mapOf(
    "hello" to "world",
    "test" to 123
)
```

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0010-Parsing.kt)


### Simple Serializing

```kotlin
JSONSerializer.default().serializeValue(mapOf(
    "hello" to "world",
    "test" to 123
))

// returns a string:
// {"hello":"world","test":123}
```

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0020-Serializing.kt)


### Using Reader and Writer

While the examples above parse and return JSON as `String` you can also use `Reader` and `Writer`:

```kotlin
val reader: Reader = …
… = JSONParser.default().parseValue(source = reader)

val writer: Writer = …
JSONSerializer.default().serializeValue(…, destination = writer)
```

Full example [for Reader](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0011-ParsingFromReader.kt)
and [for Writer](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0021-SerializingToWriter.kt)


### Parsing Lists and Maps

The methods below return the expected type directly or `null` if the input string is `"null"`.  
Any other input value causes a `JSONException`.

```kotlin
val parser = JSONParser.default()

parser.parseList(…)                     // returns List<Any>?
parser.parseListOfType<String>(…)       // returns List<String>?
parser.parseMap(…)                      // returns Map<String,Any>?
parser.parseMapOfType<String,String>(…) // returns Map<String,String>?

// you can also specify nullability
parser.parseList(…, JSONNullability.Value)                     // returns List<Any?>?
parser.parseListOfType<String>(…, JSONNullability.Value)       // returns List<String?>?
parser.parseMap(…, JSONNullability.Value)                      // returns Map<String,Any?>?
parser.parseMapOfType<String,String>(…, JSONNullability.Value) // returns Map<String,String?>?
```

Full example [for Lists](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0012-ParsingLists.kt)
and [for Maps](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0013-ParsingMaps.kt)


### Streaming Parser

`JSONReader` provides an extensive API for reading JSON values from a `Reader`.

```kotlin
val input = StringReader("""{ "data": [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ] }""")
JSONReader.build(input).use { reader ->
    reader.readFromMapByElementValue { key ->
        println(key)
        
        readFromListByElement {
            println(readInt())
        }
    }
}
```

Full example
[using high-order functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0014-ParsingAsStream.kt) and
[using low-level functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0015-ParsingAsStreamLowLevel.kt)


### Streaming Writer

`JSONWriter` provides an extensive API for writing JSON values to a `Writer`.

```kotlin
val output = StringWriter()
JSONWriter.build(output).use { writer ->
    writer.writeIntoMap {
        writeMapElement("data") {
            writeIntoList {
                for (value in 0 .. 10) {
                    json.writeInt(value)
                }
            }
        }
    }
}
```

Full example
[using high-order functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0022-SerializingAsStream.kt) and
[using low-level functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0023-SerializingAsStreamLowLevel.kt)


### Type Encoders

While many basic Kotlin types like `String`, `List`, `Map` and `Boolean` are serialized automatically to their
respective JSON counterparts you can easily add support for other types. Just write a codec for the type you'd like to
serialize by implementing `JSONEncoderCodec` and pass an instance to the builder of either `JSONSerializer` (high-level)
or `JSONEncoder` (streaming).

Codecs in turn can write other encodable values and `JSONEncoder` will automatically look up the right codec and use it
to serialize these values.

If your codec encounters an inappropriate value which it cannot encode then it can simply throw a `JSONException` in
order to stop the serialization process.

```kotlin
data class MyType(…)

object MyTypeCodec : JSONEncoderCodec<MyType, JSONCoderContext> {

    override fun encode(value: MyType, encoder: JSONEncoder<out JSONCoderContext>) {
        // write JSON for `value` directly using `encoder`  
    }


    override val encodableClasses = setOf(MyType::class)
}
```

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0030-TypeEncoders.kt)


### Type Decoders

While all JSON types are parsed automatically using appropriate Kotlin couterparts like `String`, `List`, `Map` and
`Boolean` you can easily add support for other types. Just write a codec for the type you'd like to parse by
implementing `JSONDecoderCodec` and pass an instance to the builder of either `JSONParser` (high-level) or `JSONDecoder`
(streaming).

Codecs in turn can read other decodable values and `JSONDecoder` will automatically look up the right codec and use it
to parse these values.

If your codec encounters inappropriate JSON data which it cannot decode then it can simply throw a `JSONException` in
order to stop the parsing process.

```kotlin
data class MyType(…)

object MyTypeCodec : JSONDecoderCodec<MyType, JSONCoderContext> {

    override fun decode(decoder: JSONDecoder<out JSONCoderContext>): MyType {
        // read JSON using `decoder` and create an instance of `MyType`  
    }


    override val decodableClass = MyType::class
}
```

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0031-TypeDecoders.kt)


### Type Coders

If you want to be able to encode and decode the same type you can implement the interface `JSONCodec` which in turn
extends `JSONEncoderCodec` and `JSONDecoderCodec`. That way you can reuse the same codec for encoding and decoding.

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0032-TypeCoders.kt)


### Coding and Streaming

You can use encoding and decoding codecs not just for high-level encoding and decoding using `JSONSerializer` and
`JSONParser` but also for streaming-based encoding and decoding using `JSONEncoder` and `JSONDecoder`. 

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0033-CodingAsStream.kt)


### Error Handling

Errors occuring during I/O operations in the underlying `Reader` or `Writer` cause an `IOException`.  
Errors occuring due to unsupported or mismatching types, malformed JSON or misused API cause a `JSONException`.

Since in Kotlin every method can throw any kind of exception it's recommended to simply catch `Exception` when encoding
or decoding JSON - unless handling errors explicitly is not needed in your use-case. This is especially important if you
parse JSON data from an unsafe source like a public API.  


### Thread Safety

Only the default implementations of `JSONParser`, `JSONSerializer` and `JSONCodecResolver` are thread-safe and can be
used from multiple threads without synchronization. It's strongly advised, though not required, that other
implementations of these three interfaces are also thread-safe by default.

All other interfaces are not thread-safe and must be used with approriate synchronization in place. It's recommended
however to simply use a separate instance per thread and not share these mutable instances at all.



Testing
-------

This library is tested automatically using
[extensive unit tests](https://github.com/fluidsonic/fluid-json/tree/master/Tests/Sources). Some parser tests are
imported directly from [JSONTestSuite](https://github.com/nst/JSONTestSuite) (kudos to
[Nicolas Seriot](https://github.com/nst) for that). 

Note that until [KT-12605](https://youtrack.jetbrains.com/issue/KT-12605) is fixed the code coverage
[reported by Codecov](https://codecov.io/github/fluidsonic/fluid-json) (and measured using
[JaCoCo](http://www.eclemma.org/jacoco)) is a lot lower than it should be. It's more likely around 94% - 98%.

You can run the tests manually using IntelliJ with [Spek](https://plugins.jetbrains.com/plugin/8564-spek) plugin, the
Gradle run configuration `Test` or from the command line by using:

```bash
./gradlew check
```



Type Mapping
------------

### Basic Types

#### Encoding

The default implementations of `JSONWriter` and `JSONSerializer` encode Kotlin types as follows:

| Kotlin         | JSON               | Remarks
| -------------- | ------------------ | -------
| `Array<*>`     | `array<*>`         |
| `Boolean`      | `boolean`          |
| `BooleanArray` | `array<boolean>`   |
| `Byte`         | `number`           |
| `ByteArray`    | `array<number>`    |
| `Double`       | `number`           | must be finite
| `DoubleArray`  | `array<number>`    |
| `Float`        | `number`           | must be finite
| `FloatArray`   | `array<number>`    |
| `Int`          | `number`           |
| `IntArray`     | `array<number>`    |
| `Iterable<*>`  | `array<*>`         | unless it's a `Map<*,*>`
| `Long`         | `number`           |
| `LongArray`    | `array<number>`    |
| `Map<*,*>`     | `object<string,*>` | key must be `String`
| `Number`       | `number`           | unless matched by subclass; encodes as `toDouble()`
| `Sequence<*>`  | `array<*>`         |
| `Short`        | `number`           |
| `ShortArray`   | `array<number>`    |
| `String`       | `string`           |
| `null`         | `null`             |

#### Decoding

The default implementations of `JSONReader` and `JSONParser` decode JSON types as follows:

| JSON               | Kotlin           | Remarks
| ------------------ | ---------------- | -------
| `array<*>`         | `List<*>`        |
| `boolean`          | `Boolean`        |
| `null`             | `null`           |
| `number`           | `Int`            | if number doesn't include `.` (decimal separator) or `e` (exponent separator) and fits into `Int`  
| `number`           | `Long`           | if number doesn't include `.` (decimal separator) or `e` (exponent separator) and fits into `Long` 
| `number`           | `Double`         | otherwise
| `object<string,*>` | `Map<String,*>`  |
| `string`           | `String`         |



Architecture
------------

-   At the lowest level there are `JSONReader`/`JSONWriter` which read/write JSON as a stream of `JSONToken`s:
    - character-level input/output
    - validation of read/written syntax
    - one instance per parsing/serializing (maintains state & holds reference to `Reader`/`Writer`)
-   Built on top are `JSONDecoder`/`JSONEncoder` which decode/encode Kotlin types from/to a stream of `JSONToken`s:
    - most read/write operations are forwarded to the underlying `JSONReader`/`JSONWriter`
    - some read/write operations are intercepted by `JSONEncoder` to encode compatible types using codecs
    - implementations provided by `JSONDecoderCodec`s and `JSONEncoderCodec`s
    - one instance per parsing/serializing (holds reference to `JSONReader`/`JSONWriter`)
-   Built on top are `JSONParser`/`JSONSerializer` which read/write a complete JSON value at once.
    - completely hides usage of underlying `JSONDecoder`/`JSONEncoder`
    - encoding is performed using the actual type of values to be encoded
    - decoding is performed using the type expected by the caller of `JSONParser`'s `parse…` methods and thus is only
      possible at the top-level and by `JSONDecoderCodec` implementations
    - instance can be reused and creates one `JSONDecoder`/`JSONEncoder` per parsing/serializing
    - ease of use is important
    
Most public API is provided as `interface`s in order to allow for plugging in custom behavior and to allow easy unit
testing of code which produces or consumes JSON.

The default implementations of `JSONDecoder`/`JSONEncoder` use a set of pre-defined codecs in order to support
decoding/encoding various basic Kotlin types like `String`, `List`, `Map`, `Boolean` and so on.

### Recursive vs. Non-Recursive

While codec-based decoding/encoding is supposed to be recursive in order to be efficient and easy to implement it's
sometimes not desirable to parse/serialize JSON recursively. For that reason `AnyJSONCodec` provides encoding/decoding
for all [basic types](#Basic-Types) in a non-recusive way. Since it reads/writes a whole value using
`JSONReader`'s/`JSONWriter`'s primitive `read*`/`write*` methods it will not use any other codecs and thus not support
other types.

`JSONParser.default()` and `JSONSerializer.default()` both operate solely on `AnyJSONCodec` and are thus a non-recursive
parser/serializer by default.



Future Planning
---------------

This is on the backlog for later consideration, in no specific order:

- [Add extended set of standard codecs (e.g. for `java.time`)](https://github.com/fluidsonic/fluid-json/issues/17)
- [Add annotation-based preprocessor for automatic create codecs](https://github.com/fluidsonic/fluid-json/issues/16)
- [Add performance testing](https://github.com/fluidsonic/fluid-json/issues/4)
- [Add low-level support for `BigDecimal` / `BigInteger`](https://github.com/fluidsonic/fluid-json/issues/18)
- [Add pretty serialization](https://github.com/fluidsonic/fluid-json/issues/15)
- [Improve performance by operating on `InputStream`/`OutputStream`](https://github.com/fluidsonic/fluid-json/issues/9)


License
-------

MIT

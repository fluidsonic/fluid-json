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
    implementation("com.github.fluidsonic:fluid-json:0.9.1")
}
```


Examples
--------

Check out the [Examples](https://github.com/fluidsonic/fluid-json/tree/master/Examples) directory. If you've checked out
this project locally then you can run them directly from within [IntelliJ IDEA](https://www.jetbrains.com/idea/).



Usage
-----

### Simple Parsing

```kotlin
… = JSONParser.default.parseValue("""{ "hello": "world", "test": 123 }""")

// returns a value like this:
mapOf(
    "hello" to "world",
    "test" to 123
)
```

You can also accept a `null` value by using `parseValueOrNull` instead.

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0010-Parsing.kt)


### Simple Serializing

```kotlin
JSONSerializer.default.serializeValue(mapOf(
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
… = JSONParser.default.parseValue(source = reader)

val writer: Writer = …
JSONSerializer.default.serializeValue(…, destination = writer)
```

Full example [for Reader](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0011-ParsingFromReader.kt)
and [for Writer](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0021-SerializingToWriter.kt)


### Parsing Lists and Maps

You can also parse lists and maps in a type-safe way directly. Should it not be possible to parse the input as the
requested Kotlin type a `JSONException` is thrown.

```kotlin
val parser = JSONParser.default

parser.parseValueOfType<List<*>>(…)              // returns List<*>
parser.parseValueOfType<List<String?>>(…)        // returns List<String?>
parser.parseValueOfType<Map<*,*>>(…)             // returns Map<*,*>
parser.parseValueOfType<Map<String,String?>>(…)  // returns Map<String,String?>
```

Note that you can also specify non-nullable `String` instead of nullable `String?`. But due to a limitation of Kotlin
and the JVM the resulting list/map can always contain `null` keys and values. This can cause an unexpected
`NullPointerException` at runtime if the source data contains `null`s.

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
[using higher-order functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0014-ParsingAsStream.kt) and
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
[using higher-order functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0022-SerializingAsStream.kt) and
[using low-level functions](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0023-SerializingAsStreamLowLevel.kt)


### Type Encoder Codecs

While many basic Kotlin types like `String`, `List`, `Map` and `Boolean` are serialized automatically to their
respective JSON counterparts you can easily add support for other types. Just write a codec for the type you'd like to
serialize by implementing `JSONEncoderCodec` and pass an instance to the builder of either `JSONSerializer`
(high-level API) or `JSONEncoder` (streaming API).

Codecs in turn can write other encodable values and `JSONEncoder` will automatically look up the right codec and use it
to serialize these values.

If your codec encounters an inappropriate value which it cannot encode then it will throw a `JSONException` in order to
stop the serialization process.

Because `JSONEncoderCodec` is simply an interface you can use `AbstractJSONEncoderCodec` as base class for your codec
which simplifies implementing that interface.

```kotlin
data class MyType(…)

object MyTypeCodec : AbstractJSONEncoderCodec<MyType, JSONCoderContext>() {

    override fun encode(value: MyType, encoder: JSONEncoder<JSONCoderContext>) {
        // write JSON for `value` directly using `encoder`
    }
}
```

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0030-TypeEncoderCodecs.kt)


### Type Decoder Codecs

While all JSON types are parsed automatically using appropriate Kotlin couterparts like `String`, `List`, `Map` and
`Boolean` you can easily add support for other types. Just write a codec for the type you'd like to parse by
implementing `JSONDecoderCodec` and pass an instance to the builder of either `JSONParser` (high-level API) or
`JSONDecoder` (streaming API).

Codecs in turn can read other decodable values and `JSONDecoder` will automatically look up the right codec and use it
to parse these values.

If your codec encounters inappropriate JSON data which it cannot decode then it will throw a `JSONException` in order to
stop the parsing process.

Because `JSONDecoderCodec` is simply an interface you can use `AbstractJSONDecoderCodec` as base class for your codec
which simplifies implementing that interface.

```kotlin
data class MyType(…)

object MyTypeCodec : AbstractJSONDecoderCodec<MyType, JSONCoderContext>() {

    override fun decode(valueType: JSONCodableType<in MyType>, decoder: JSONDecoder<JSONCoderContext>): MyType {
        // read JSON using `decoder` and create an instance of `MyType`
    }
}
```

A `JSONDecoderCodec` can also decode generic types. The instance passed to `JSONCodableType` contains information about
generic arguments expected by the call which caused this codec to be invoked. For `List<Something>` for example a single
generic argument of type `Something` would be reported which allows for example the list codec to serialize the list
value's directly as `Something` using the respective codec.

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0031-TypeDecoderCodecs.kt)


### Type Codecs

If you want to be able to encode and decode the same type you can implement the interface `JSONCodec` which in turn
extends `JSONEncoderCodec` and `JSONDecoderCodec`. That way you can reuse the same codec class for both, encoding and
decoding.

Because `JSONCodec` is simply an interface you can use `AbstractJSONCodec` as base class for your codec which simplifies
implementing that interface.

[Full example](https://github.com/fluidsonic/fluid-json/blob/master/Examples/0032-TypeCodecs.kt)


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

All implementations of `JSONParser`, `JSONSerializer`, `JSONCodecProvider` as well as all codecs provided by this
library are thread-safe and can be used from multiple threads without synchronization. It's strongly advised, though not
required, that custom implementations are also thread-safe by default.

All other classes and interfaces are not thread-safe and must be used with approriate synchronization in place. It's
recommended however to simply use a separate instance per thread and not share these mutable instances at all.



Testing
-------

This library is tested automatically using
[extensive unit tests](https://github.com/fluidsonic/fluid-json/tree/master/Tests/Sources). Some parser tests are
imported directly from [JSONTestSuite](https://github.com/nst/JSONTestSuite) (kudos to
[Nicolas Seriot](https://github.com/nst) for that).

Note that until [KT-12605](https://youtrack.jetbrains.com/issue/KT-12605) is fixed the code coverage
[reported by Codecov](https://codecov.io/github/fluidsonic/fluid-json) (and measured using
[JaCoCo](http://www.eclemma.org/jacoco)) is a lot lower than it should be.

You can run the tests manually using IntelliJ IDEA and the [Spek](https://plugins.jetbrains.com/plugin/8564-spek)
plugin, the Gradle run configuration `Test` or from the command line by using:

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

### Extended Types

The following classes of the `java.time` package can also be decoded and encoded out of the box:

| Kotlin           | JSON     | Remarks
| ---------------- | -------- | -------
| `DayOfWeek`      | `string` | `"monday"`, …, `"friday"`
| `Duration`       | `string` | using `.parse()` / `.toString()`
| `Instant`        | `string` | using `.parse()` / `.toString()`
| `LocalDate`      | `string` | using `.parse()` / `.toString()`
| `LocalDateTime`  | `string` | using `.parse()` / `.toString()`
| `LocalTime`      | `string` | using `.parse()` / `.toString()`
| `MonthDay`       | `string` | using `.parse()` / `.toString()`
| `Month`          | `string` | `"january"`, …, `"december"`
| `OffsetDateTime` | `string` | using `.parse()` / `.toString()`
| `OffsetTime`     | `string` | using `.parse()` / `.toString()`
| `Period`         | `string` | using `.parse()` / `.toString()`
| `Year`           | `int`    | using `.value`
| `YearMonth`      | `string` | using `.parse()` / `.toString()`
| `ZonedDateTime`  | `string` | using `.parse()` / `.toString()`
| `ZoneId`         | `string` | using `.of()` / `.id`
| `ZoneOffset`     | `string` | using `.of()` / `.id`



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
    - inspired by MongoDB's [Codec and CodecRegistry](http://mongodb.github.io/mongo-java-driver/3.6/bson/codecs/)
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

While codec-based decoding/encoding has to be implemented recursively in order to be efficient and easy to use it's
sometimes not desirable to parse/serialize JSON recursively. For that reason the default container codecs like
`MapJSONCodec` also provide a `nonRecursive` codec. Since they read/write a whole value at once using
`JSONReader`'s/`JSONWriter`'s primitive `read*`/`write*` methods they will not use any other codecs and thus not support
other types.

`JSONParser.nonRecursive` and `JSONSerializer.nonRecursive` both operate on these codecs and are thus a non-recursive
parser/serializer.

### Classes and Interfaces

| Type                       | Description
| -------------------------- | -----------
| `AbstractJSONCodec`        | Abstract base class which simplifies implementing `JSONCodec`.
| `AbstractJSONDecoderCodec` | Abstract base class which simplifies implementing `JSONDecoderCodec`.
| `AbstractJSONEncoderCodec` | Abstract base class which simplifies implementing `JSONEncoderCodec`.
| `DefaultJSONCodecs`        | Contains lists of default codecs which can be used when contructing custom `JSONCodecProvider`s.
| `JSONCodableType`          | Roughly describes a Kotlin type which can be decoded from JSON. It includes relevant generic information which allows decoding for example `List<Something>` instead of just `List<*>`. Also known as [type token](http://gafter.blogspot.de/2006/12/super-type-tokens.html)).
| `JSONCodec`                | Interface for classes which implement both, `JSONEncoderCodec` and `JSONDecoderCodec`. Also simplifies creating such codecs.
| `JSONCodecProvider`        | Interface for classes which when given a `JSONCodableType` (for decoding) or `KClass` (for encoding) return a codec which is able to decode/encode values of that type.
| `JSONCoderContext`         | Interface for context types. Instances of context types can be passed to `JSONParser`, `JSONSerializer`, `JSONDecoder` and `JSONEncoder`. They in turn can be used by codecs to help decoding/encoding values if needed.
| `JSONDecoder`              | Interface which extends `JSONReader` to enable reading values of any Kotlin type from JSON using `JSONCodecProvider`s for type mapping.
| `JSONDecoderCodec`         | Interface for decoding a value of a specific Kotlin type using a `JSONDecoder`.
| `JSONEncoder`              | Interface which extends `JSONWriter` to enable writing values of any Kotlin type as JSON using `JSONCodecProvider`s for type mapping.
| `JSONEncoderCodec`         | Interface for encoding a value of a specific Kotlin type using a `JSONEncoder`.
| `JSONException`            | Exception which is thrown whenever JSON cannot be written or read for non-IO reasons (e.g. malformed JSON, wrong state in reader/writer, missing type mapping).
| `JSONParser`               | Interface for high-level JSON parsing where codec providers and context are already configured.
| `JSONReader`               | Interface for low-level JSON parsing on a token-by-token basis.
| `JSONSerializer`           | Interface for high-level JSON serialization where codec providers and context are already configured.
| `JSONToken`                | Enum containing all types of tokens a `JSONReader` can read.
| `JSONWriter`               | Interface for low-level JSON serialization on a token-by-token basis.
| `*Codec`                   | The various codec classes are concrete codecs for common Kotlin types.



Future Planning
---------------

This is on the backlog for later consideration, in no specific order:

- [Add KDoc to all public API](https://github.com/fluidsonic/fluid-json/issues/28)
- [Add annotation-based preprocessor for automatic create codecs](https://github.com/fluidsonic/fluid-json/issues/16)
- [Add performance testing](https://github.com/fluidsonic/fluid-json/issues/4)
- [Add low-level support for `BigDecimal` / `BigInteger`](https://github.com/fluidsonic/fluid-json/issues/18)
- [Add pretty serialization](https://github.com/fluidsonic/fluid-json/issues/15)
- [Improve performance by operating on `InputStream`/`OutputStream`](https://github.com/fluidsonic/fluid-json/issues/9)
- [Add standard decoders for array types](https://github.com/fluidsonic/fluid-json/issues/23)
- [Add a codec for enums](https://github.com/fluidsonic/fluid-json/issues/24)
- [Wrap (some) other throwables in JSONException](https://github.com/fluidsonic/fluid-json/issues/25)
- [Ensure that codecs operate solely on value boundaries](https://github.com/fluidsonic/fluid-json/issues/26)
- [Split up into two libraries](https://github.com/fluidsonic/fluid-json/issues/22)



License
-------

MIT

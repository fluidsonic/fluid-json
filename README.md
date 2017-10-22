fluid-json
==========

[![Build Status](https://travis-ci.org/fluidsonic/fluid-json.svg?branch=master)](https://travis-ci.org/fluidsonic/fluid-json)
[![codecov.io](https://codecov.io/github/fluidsonic/fluid-json/coverage.svg?branch=master)](https://codecov.io/github/fluidsonic/fluid-json?branch=master)

A JSON library written for Kotlin, in Kotlin.



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

In the [Examples](https://github.com/fluidsonic/fluid-json/tree/master/Examples) directory. If you've checked out this
project locally you can run them directly from within IntelliJ.



Usage
-----

### Simple Parsing

```kotlin
JSONParser.default().parseValue("""{ 
	"hello": "world",
	"test":  123
}""")

// returns a value like this:
mapOf(
    "hello" to "world",
    "test" to 123
)
```

### Simple Serializing

```kotlin
JSONSerializer.default().serializeValue(mapOf(
	"hello" to "world",
	"test" to 123
))

// returns a string:
// {"hello":"world","test":123}
```

### Using Reader and Writer

While the examples above parse and return JSON as `String` you can also use `Reader` and `Writer`:

```kotlin
val reader: Reader = …
JSONParser.default().parseValue(source = reader)

val writer: Writer = …
JSONSerializer.default().serializeValue(…, destination = writer)
```

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

### Errors

Errors occuring during I/O operations in the underlying `Reader` or `Writer` throw an `IOException`.  
Errors occuring due to unsupported or mismatching types, malformed JSON or misused API throw a `JSONException`.


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



License
-------

MIT

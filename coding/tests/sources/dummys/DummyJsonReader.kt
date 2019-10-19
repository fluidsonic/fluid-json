package tests.coding

import io.fluidsonic.json.*


internal open class DummyJsonReader : JsonReader {

	override fun beginValueIsolation() = JsonDepth(0)
	override fun close(): Unit = error("")
	override val depth: JsonDepth get() = error("")
	override fun endValueIsolation(depth: JsonDepth) {}
	override val isInValueIsolation: Boolean get() = false
	override val nextToken: JsonToken? get() = error("")
	override val offset: Int get() = error("")
	override val path: JsonPath get() = error("")
	override fun readBoolean(): Boolean = error("")
	override fun readDouble(): Double = error("")
	override fun readListEnd(): Unit = error("")
	override fun readListStart(): Unit = error("")
	override fun readLong(): Long = error("")
	override fun readMapEnd(): Unit = error("")
	override fun readMapStart(): Unit = error("")
	override fun readNull(): Nothing? = error("")
	override fun readNumber(): Number = error("")
	override fun readString(): String = error("")
	override fun terminate(): Unit = error("")
}

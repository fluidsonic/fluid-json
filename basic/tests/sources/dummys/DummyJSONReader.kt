package tests.basic

import com.github.fluidsonic.fluid.json.*


internal open class DummyJSONReader : JSONReader {

	override fun beginValueIsolation() = JSONDepth(0)
	override fun close(): Unit = error("")
	override val depth: JSONDepth get() = error("")
	override fun endValueIsolation(depth: JSONDepth) {}
	override val isInValueIsolation: Boolean get() = false
	override val nextToken: JSONToken? get() = error("")
	override val offset: Int get() = error("")
	override val path: JSONPath get() = error("")
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

package tests.coding

import com.github.fluidsonic.fluid.json.*


internal open class DummyJSONReader : JSONReader {
	override fun close(): Unit = error("")
	override val nextToken get(): JSONToken? = error("")
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

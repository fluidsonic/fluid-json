package tests.basic

import io.fluidsonic.json.*


internal open class DummyJsonWriter : JsonWriter {
	override fun beginValueIsolation() = JsonDepth(0)
	override val depth: JsonDepth get() = error("")
	override fun endValueIsolation(depth: JsonDepth) {}
	override val isInValueIsolation: Boolean get() = error("")
	override val isErrored: Boolean get() = error("")
	override val path: JsonPath get() = error("")
	override fun markAsErrored(): Unit = error("")
	override fun terminate(): Unit = error("")
	override fun writeBoolean(value: Boolean): Unit = error("")
	override fun writeDouble(value: Double): Unit = error("")
	override fun writeListEnd(): Unit = error("")
	override fun writeListStart(): Unit = error("")
	override fun writeLong(value: Long): Unit = error("")
	override fun writeMapEnd(): Unit = error("")
	override fun writeMapStart(): Unit = error("")
	override fun writeNull(): Unit = error("")
	override fun writeString(value: String): Unit = error("")
	override fun close(): Unit = error("")
	override fun flush(): Unit = error("")
}

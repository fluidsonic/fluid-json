package tests


internal object Resources {

	fun stream(name: String) =
		Resources::class.java.getResourceAsStream("/$name")
			?: throw IllegalArgumentException("Resource not found: $name")
}

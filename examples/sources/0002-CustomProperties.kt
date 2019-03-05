package examples

import com.github.fluidsonic.fluid.json.*

// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt, so enable this setting first:
// Preferences > Build, Execution, Deployment > Build Tools > Gradle > Runner > Delegate IDE build/run actions to gradle

// @JSON.CustomProperties allows writing custom code to add properties on-the-fly, potentially making use of a context object.


fun main() {
	val serializer = JSONCodingSerializer
		.builder(MyContext(authenticatedUserId = "5678"))
		.encodingWith(JSONCodecProvider.generated(MyCodecProvider::class))
		.build()

	println(serializer.serializeValue(listOf(
		User(id = "1234", name = "Some Other User", emailAddress = "email@hidden.com"),
		User(id = "5678", name = "Authenticated User", emailAddress = "own@email.com")
	)))
}


@JSON(
	decoding = JSON.Decoding.none,                // prevent decoding altogether
	encoding = JSON.Encoding.annotatedProperties  // only encode properties annotated explicitly
)
data class User(
	@JSON.Property val id: String,
	@JSON.Property val name: String,
	val emailAddress: String
)


@JSON.CustomProperties  // function will be called during encoding
fun JSONEncoder<MyContext>.writeCustomProperties(value: User) {
	if (context.authenticatedUserId == value.id)
		writeMapElement("emailAddress", value = value.emailAddress)
}

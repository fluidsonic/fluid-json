package examples

import io.fluidsonic.json.*

// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt, so enable this setting first:
// Preferences > Build, Execution, Deployment > Build Tools > Gradle > Runner > Delegate IDE build/run actions to gradle

// @Json.CustomProperties allows writing custom code to add properties on-the-fly, potentially making use of a context object.


fun main() {
	val serializer = JsonCodingSerializer
		.builder(MyContext(authenticatedUserId = "5678"))
		.encodingWith(JsonCodecProvider.generated(MyCodecProvider::class))
		.build()

	println(serializer.serializeValue(listOf(
		User(id = "1234", name = "Some Other User", emailAddress = "email@hidden.com"),
		User(id = "5678", name = "Authenticated User", emailAddress = "own@email.com")
	)))
}


@Json(
	decoding = Json.Decoding.none,                // prevent decoding altogether
	encoding = Json.Encoding.annotatedProperties  // only encode properties annotated explicitly
)
data class User(
	@Json.Property val id: String,
	@Json.Property val name: String,
	val emailAddress: String
)


@Json.CustomProperties  // function will be called during encoding
fun JsonEncoder<MyContext>.writeCustomProperties(value: User) {
	if (context.authenticatedUserId == value.id)
		writeMapElement("emailAddress", value = value.emailAddress)
}

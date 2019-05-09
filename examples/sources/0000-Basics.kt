package examples

import com.github.fluidsonic.fluid.json.*
import java.time.*

// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt, so enable this setting first:
// Preferences > Build, Execution, Deployment > Build Tools > Gradle > Runner > Delegate IDE build/run actions to gradle

// EventJSONCodec and AttendeeJSONCodec are generated automatically for the @JSON-annotated classes in this file

fun main() {
	val data = Event(
		attendees = listOf(
			Attendee(emailAddress = "marc@knaup.io", firstName = "Marc", lastName = "Knaup", rsvp = RSVP.going),
			Attendee(emailAddress = "john@doe.com", firstName = "John", lastName = "Doe", rsvp = null)
		),
		description = "Discussing the fluid-json library.",
		end = Instant.now() + Duration.ofHours(2),
		id = 1,
		start = Instant.now(),
		title = "fluid-json MeetUp"
	)

	val serializer = JSONCodingSerializer.builder(MyContext()) // see example 0001
		.encodingWith(EventJSONCodec, AttendeeJSONCodec)
		.build()

	val serialized = serializer.serializeValue(data)
	println("serialized: $serialized")

	val parser = JSONCodingParser.builder(MyContext()) // see example 0001
		.decodingWith(EventJSONCodec, AttendeeJSONCodec)
		.build()

	val parsed = parser.parseValueOfType<Event>(serialized)
	println("parsed: $parsed")
}


@JSON
data class Event(
	val attendees: Collection<Attendee>,
	val description: String,
	val end: Instant,
	val id: Int,
	val start: Instant,
	val title: String
)

@JSON
data class Attendee(
	val emailAddress: String,
	val firstName: String,
	val lastName: String,
	val rsvp: RSVP?
)

enum class RSVP {
	notGoing,
	going
}

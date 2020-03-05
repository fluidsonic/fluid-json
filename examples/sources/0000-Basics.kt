package examples

import io.fluidsonic.json.*
import java.time.*

// Note that IntelliJ IDEA still only has limited supported for annotation processing with kapt, so enable this setting first:
// Preferences > Build, Execution, Deployment > Build Tools > Gradle > Build an run using: > Gradle

// EventJsonCodec and AttendeeJsonCodec are generated automatically for the @Json-annotated classes in this file

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

	val serializer = JsonCodingSerializer.builder(MyContext()) // see example 0001
		.encodingWith(EventJsonCodec, AttendeeJsonCodec)
		.build()

	val serialized = serializer.serializeValue(data)
	println("serialized: $serialized")

	val parser = JsonCodingParser.builder(MyContext()) // see example 0001
		.decodingWith(EventJsonCodec, AttendeeJsonCodec)
		.build()

	val parsed = parser.parseValueOfType<Event>(serialized)
	println("parsed: $parsed")
}


@Json
data class Event(
	val attendees: Collection<Attendee>,
	val description: String,
	val end: Instant,
	val id: Int,
	val start: Instant,
	val title: String
)

@Json
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

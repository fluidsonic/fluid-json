package tests.coding


internal data class YearMonthDay(
	val year: Int,
	val month: Int,
	val day: Int
) {

	override fun toString() =
		buildString {
			append(year)
			append('-')

			if (month < 10) append('0')
			append(month)
			append('-')

			if (day < 10) append('0')
			append(day)
		}


	companion object {

		private val regex = Regex("^(\\d{1,8})-(\\d{1,2})-(\\d{1,2})")


		fun parse(string: String): YearMonthDay? {
			val result = regex.matchEntire(string) ?: return null

			return YearMonthDay(
				year = result.groupValues[1].toInt(),
				month = result.groupValues[2].toInt(),
				day = result.groupValues[3].toInt()
			)
		}
	}
}

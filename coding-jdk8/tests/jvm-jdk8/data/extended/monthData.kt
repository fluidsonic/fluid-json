package tests.coding

import java.time.*


internal val monthData: TestData<Month> = TestData(
	symmetric = mapOf(
		Month.JANUARY to "\"january\"",
		Month.FEBRUARY to "\"february\"",
		Month.MARCH to "\"march\"",
		Month.APRIL to "\"april\"",
		Month.MAY to "\"may\"",
		Month.JUNE to "\"june\"",
		Month.JULY to "\"july\"",
		Month.AUGUST to "\"august\"",
		Month.SEPTEMBER to "\"september\"",
		Month.OCTOBER to "\"october\"",
		Month.NOVEMBER to "\"november\"",
		Month.DECEMBER to "\"december\""
	)
)

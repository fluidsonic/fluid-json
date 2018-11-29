package tests.coding

import java.math.BigDecimal


internal val bigDecimalData: TestData<BigDecimal> = TestData(
	encodableOnly = mapOf(
		(-1E200).toBigDecimal() to "-1.0E200",
		(-100.999).toBigDecimal() to "-100.999",
		(-100.001).toBigDecimal() to "-100.001",
		(-1).toBigDecimal() to "-1.0",
		(-1E-200).toBigDecimal() to "-1.0E-200",
		(-0.0).toBigDecimal() to "-0.0",
		0.toBigDecimal() to "0.0",
		0.0.toBigDecimal() to "0.0",
		1E-200.toBigDecimal() to "1.0E-200",
		1.toBigDecimal() to "1.0",
		100.001.toBigDecimal() to "100.001",
		100.999.toBigDecimal() to "100.999",
		1E200.toBigDecimal() to "1.0E200"
	)
)

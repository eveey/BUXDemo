package com.evastos.bux.data.model.rtf.update

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class UpdateEventBody(
    @Json(name = "securityId") val securityId: String?,
    @Json(name = "currentPrice") val currentPrice: BigDecimal?
)

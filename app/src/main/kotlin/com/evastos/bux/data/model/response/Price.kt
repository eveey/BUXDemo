package com.evastos.bux.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Price(
    @Json(name = "currency") val currency: String?,
    @Json(name = "decimals") val decimals: Int?,
    @Json(name = "amount") val amount: Double?
)
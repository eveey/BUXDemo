package com.evastos.bux.ui.util

import timber.log.Timber
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * Wrapper around Locale, NumberFormat and Currency classes.
 * Utility class for displaying locale-aware formatted prices.
 */
class PriceUtil constructor() {

    companion object {
        private const val DEFAULT_DECIMAL_PLACES = 2
        private val percentFull = 100.toBigDecimal()
    }

    /**
     * Returns formatted locale-aware price, provided the currency code is in ISO 4217 format.
     */
    fun getLocalisedPrice(
        price: BigDecimal?,
        currencyCode: String?,
        decimalPlaces: Int? = null
    ): String {
        if (price == null || currencyCode == null) {
            return ""
        }
        val currencyNumberFormat = NumberFormat.getCurrencyInstance()
        val currency = getCurrency(currencyCode)
        currency?.let {
            currencyNumberFormat.currency = it
        }
        val minimumFractionDigits = decimalPlaces ?: DEFAULT_DECIMAL_PLACES
        currencyNumberFormat.minimumFractionDigits = minimumFractionDigits
        currencyNumberFormat.maximumFractionDigits = minimumFractionDigits
        return currencyNumberFormat.format(price)
    }

    /**
     * Returns formatted locale-aware price difference percent.
     */
    fun getPriceDifferencePercent(
        previousPrice: BigDecimal?,
        currentPrice: BigDecimal?
    ): String {
        if (previousPrice == null || currentPrice == null) {
            return ""
        }
        val differencePercent: BigDecimal = if (previousPrice == BigDecimal.ZERO) {
            percentFull
        } else if (currentPrice == BigDecimal.ZERO) {
            -percentFull
        } else if (currentPrice > previousPrice) {
            (currentPrice / previousPrice) * percentFull
        } else {
            -(currentPrice / previousPrice) * percentFull
        }
        val percentFormat = NumberFormat.getPercentInstance()
        percentFormat.minimumFractionDigits = 0
        percentFormat.maximumFractionDigits = 0
        return percentFormat.format(differencePercent)
    }

    private fun getCurrency(currencyCode: String?): Currency? {
        currencyCode?.let {
            try {
                return Currency.getInstance(it)
            } catch (e: IllegalArgumentException) {
                Timber.e("Unknown currency code:$it")
            }
        }
        return null
    }
}
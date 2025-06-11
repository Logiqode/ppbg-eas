package com.example.digibanker.util

import java.text.NumberFormat
import java.util.Locale

/**
 * Utility function to format a Double into a currency string.
 * Example: 7550.25 -> "$7,550.25"
 *
 * @param amount The numerical value to be formatted.
 * @return A string representing the amount in US currency format.
 */
fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}
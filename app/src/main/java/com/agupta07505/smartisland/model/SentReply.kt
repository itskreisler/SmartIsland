/*
 * Smart Island (2026)
 * © Animesh Gupta — github.com/agupta07505
 * Licensed under the GNU GPL v3 License
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.agupta07505.smartisland.model

/**
 * Card after a reply was sent: the typed message is added to the card (short and reader text) instead of closing it
 * and getting the app's echo as a new card. A re-post of the same key replaces it.
 */
fun IslandNotification.withSentReply(message: String, label: String): IslandNotification {
    val sent = message.trim()
    if (sent.isEmpty()) return this
    val line = label + sent
    val history = bigText.ifBlank { text }
    return copy(
        text = line,
        bigText = if (history.isBlank()) line else history + "\n" + line
    )
}

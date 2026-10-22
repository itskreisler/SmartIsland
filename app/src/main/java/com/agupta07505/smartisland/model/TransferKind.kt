/*
 * Smart Island (2026)
 * © Animesh Gupta — github.com/agupta07505
 * Licensed under the GNU GPL v3 License
 * Do not remove or alter this notice. - Per GPL-3.0 Section 4 & Section 5
 */

package com.agupta07505.smartisland.model

import androidx.annotation.StringRes
import com.agupta07505.smartisland.R

/**
 * What a progress card (IslandMode.DownloadUpload) is doing. Each kind has its own Home switch and wording on the
 * card; `pointsUp` picks the arrow direction of the transfer glyph.
 */
enum class TransferKind(
    val pointsUp: Boolean,
    @StringRes val badgeRes: Int,
    @StringRes val fallbackTitleRes: Int
) {
    DOWNLOAD(false, R.string.transfer_badge_downloading, R.string.transfer_title_downloading),
    UPLOAD(true, R.string.transfer_badge_uploading, R.string.transfer_title_uploading),
    BACKUP(true, R.string.transfer_badge_backing_up, R.string.transfer_title_backing_up),
    COPY_MOVE(false, R.string.transfer_badge_copying, R.string.transfer_title_copying),
    DELETE(false, R.string.transfer_badge_deleting, R.string.transfer_title_deleting),
    INSTALL(false, R.string.transfer_badge_installing, R.string.transfer_title_installing)
}
